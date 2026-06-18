package com.assetflow.backend.service;

import com.assetflow.backend.dto.asset.AssetCreateRequest;
import com.assetflow.backend.dto.asset.AssetResponse;
import com.assetflow.backend.dto.asset.AssetUpdateRequest;
import com.assetflow.backend.exception.AssetNotFoundException;
import com.assetflow.backend.exception.UserNotFoundException;
import com.assetflow.backend.mapper.AssetMapper;
import com.assetflow.backend.model.Asset;
import com.assetflow.backend.model.Role;
import com.assetflow.backend.model.User;
import com.assetflow.backend.repository.AssetRepository;
import com.assetflow.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AssetService {

    private final AssetRepository assetRepository;
    private final UserRepository userRepository;

    private String getCurrentUsername() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return authentication.getName();
    }

    private User getCurrentUser() {

        String username = getCurrentUsername();

        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    private Asset getAccessibleAsset(Long id, User user) {

        boolean isAdmin = user.getRole() == Role.ADMIN;

        return assetRepository.findById(id)
                .filter(asset -> isAdmin || asset.getUser().getId().equals(user.getId()))
                .orElseThrow(() -> new AssetNotFoundException(id));
    }

    public AssetService(AssetRepository assetRepository, UserRepository userRepository) {
        this.assetRepository = assetRepository;
        this.userRepository = userRepository;
    }

    public List<AssetResponse> getAssetsForUser() {

        User user = getCurrentUser();

        return assetRepository.findByUser(user)
                .stream()
                .map(AssetMapper::toResponse)
                .toList();
    }

    public List<AssetResponse> getAllAssetsForAdmin() {

        return assetRepository.findAll()
                .stream()
                .map(AssetMapper::toResponse)
                .toList();
    }

    public AssetResponse getAssetById(Long id) {

        User user = getCurrentUser();

        Asset asset = getAccessibleAsset(id, user);

        return AssetMapper.toResponse(asset);
    }

    public AssetResponse createAsset(AssetCreateRequest request) {

        User user = getCurrentUser();

        Asset asset = AssetMapper.toEntity(request);
        asset.setUser(user);

        Asset savedAsset = assetRepository.save(asset);

        log.info(
                "Created asset id={} for user '{}'",
                savedAsset.getId(),
                user.getUsername()
        );

        return AssetMapper.toResponse(savedAsset);
    }

    public AssetResponse updateAsset(Long id, AssetUpdateRequest request) {

        User user = getCurrentUser();

        Asset existing = getAccessibleAsset(id, user);

        AssetMapper.updateEntity(existing, request);

        Asset updated = assetRepository.save(existing);

        log.info(
                "Updated asset id={} by user '{}'",
                updated.getId(),
                user.getUsername()
        );

        return AssetMapper.toResponse(updated);
    }

    public void deleteAsset(Long id) {

        User user = getCurrentUser();

        Asset existing = getAccessibleAsset(id, user);

        assetRepository.delete(existing);

        log.info(
                "Deleted asset id={} by user '{}'",
                id,
                user.getUsername()
        );

    }
}
