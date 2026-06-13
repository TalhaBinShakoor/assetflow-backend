package com.assetflow.backend.service;

import com.assetflow.backend.dto.asset.AssetCreateRequest;
import com.assetflow.backend.dto.asset.AssetResponse;
import com.assetflow.backend.dto.asset.AssetUpdateRequest;
import com.assetflow.backend.mapper.AssetMapper;
import com.assetflow.backend.model.Asset;
import com.assetflow.backend.model.User;
import com.assetflow.backend.repository.AssetRepository;
import com.assetflow.backend.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.assetflow.backend.exception.AssetNotFoundException;

import java.util.List;

@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final UserRepository userRepository;

    private String getCurrentUsername() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String username = authentication.getName();
        System.out.println("Logged in user: " + username);
        return authentication.getName();
    }

    public AssetService(AssetRepository assetRepository, UserRepository userRepository) {
        this.assetRepository = assetRepository;
        this.userRepository = userRepository;
    }

    public List<AssetResponse> getAllAssets() {

        String username = getCurrentUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return assetRepository.findByUser(user)
                .stream()
                .map(AssetMapper::toResponse)
                .toList();
    }

    public AssetResponse getAssetById(Long id) {

        String username = getCurrentUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Asset asset = assetRepository.findById(id)
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new AssetNotFoundException(id));

        return AssetMapper.toResponse(asset);
    }

    public AssetResponse createAsset(AssetCreateRequest request) {

        String username = getCurrentUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Asset asset = AssetMapper.toEntity(request);

        // 🔥 THIS is the important line (now asset belongs to logged-in user)
        asset.setUser(user);

        Asset savedAsset = assetRepository.save(asset);

        return AssetMapper.toResponse(savedAsset);
    }

    public AssetResponse updateAsset(Long id, AssetUpdateRequest request) {

        String username = getCurrentUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Asset existing = assetRepository.findById(id)
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new AssetNotFoundException(id));

        AssetMapper.updateEntity(existing, request);

        Asset updated = assetRepository.save(existing);

        return AssetMapper.toResponse(updated);
    }

    public void deleteAsset(Long id) {

        String username = getCurrentUsername();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Asset existing = assetRepository.findById(id)
                .filter(a -> a.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new AssetNotFoundException(id));

        assetRepository.delete(existing);
    }
}