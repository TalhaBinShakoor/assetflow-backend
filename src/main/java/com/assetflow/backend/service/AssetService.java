package com.assetflow.backend.service;

import com.assetflow.backend.dto.asset.AssetCreateRequest;
import com.assetflow.backend.dto.asset.AssetResponse;
import com.assetflow.backend.dto.asset.AssetUpdateRequest;
import com.assetflow.backend.mapper.AssetMapper;
import com.assetflow.backend.model.Asset;
import com.assetflow.backend.repository.AssetRepository;
import org.springframework.stereotype.Service;

import com.assetflow.backend.exception.AssetNotFoundException;

import java.util.List;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<AssetResponse> getAllAssets() {
        return assetRepository.findAll()
                .stream()
                .map(AssetMapper::toResponse)
                .toList();
    }

    public AssetResponse getAssetById(Long id) {

        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new AssetNotFoundException(id));

        return AssetMapper.toResponse(asset);
    }

    public AssetResponse createAsset(AssetCreateRequest request) {

        Asset asset = AssetMapper.toEntity(request);

        Asset savedAsset = assetRepository.save(asset);

        return AssetMapper.toResponse(savedAsset);
    }

    public AssetResponse updateAsset(Long id, AssetUpdateRequest request) {

        Asset existing = assetRepository.findById(id)
                .orElseThrow(() -> new AssetNotFoundException(id));

        AssetMapper.updateEntity(existing, request);

        Asset updated = assetRepository.save(existing);

        return AssetMapper.toResponse(updated);
    }

    public void deleteAsset(Long id) {
        Asset existing = assetRepository.findById(id)
                .orElseThrow(() -> new AssetNotFoundException(id));

        assetRepository.delete(existing);
    }
}