package com.assetflow.backend.service;

import com.assetflow.backend.dto.asset.AssetCreateRequest;
import com.assetflow.backend.dto.asset.AssetResponse;
import com.assetflow.backend.dto.asset.AssetUpdateRequest;
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
                .map(asset -> {

                    AssetResponse response = new AssetResponse();

                    response.setId(asset.getId());
                    response.setName(asset.getName());
                    response.setCategory(asset.getCategory());
                    response.setStatus(asset.getStatus());
                    response.setPurchaseDate(asset.getPurchaseDate());

                    return response;
                })
                .toList();
    }

    public AssetResponse getAssetById(Long id) {

        Asset asset = assetRepository.findById(id)
                .orElseThrow(() -> new AssetNotFoundException(id));

        AssetResponse response = new AssetResponse();
        response.setId(asset.getId());
        response.setName(asset.getName());
        response.setCategory(asset.getCategory());
        response.setStatus(asset.getStatus());
        response.setPurchaseDate(asset.getPurchaseDate());

        return response;
    }

    public AssetResponse createAsset(AssetCreateRequest request) {

        Asset asset = new Asset();

        asset.setName(request.getName());
        asset.setCategory(request.getCategory());
        asset.setStatus(request.getStatus());
        asset.setPurchaseDate(request.getPurchaseDate());

        Asset savedAsset = assetRepository.save(asset);

        AssetResponse response = new AssetResponse();

        response.setId(savedAsset.getId());
        response.setName(savedAsset.getName());
        response.setCategory(savedAsset.getCategory());
        response.setStatus(savedAsset.getStatus());
        response.setPurchaseDate(savedAsset.getPurchaseDate());

        return response;
    }

    public AssetResponse updateAsset(Long id, AssetUpdateRequest request) {

        Asset existing = assetRepository.findById(id)
                .orElseThrow(() -> new AssetNotFoundException(id));

        existing.setName(request.getName());
        existing.setCategory(request.getCategory());
        existing.setStatus(request.getStatus());
        existing.setPurchaseDate(request.getPurchaseDate());

        Asset saved = assetRepository.save(existing);

        AssetResponse response = new AssetResponse();
        response.setId(saved.getId());
        response.setName(saved.getName());
        response.setCategory(saved.getCategory());
        response.setStatus(saved.getStatus());
        response.setPurchaseDate(saved.getPurchaseDate());

        return response;
    }

    public void deleteAsset(Long id) {
        Asset existing = assetRepository.findById(id)
                .orElseThrow(() -> new AssetNotFoundException(id));

        assetRepository.delete(existing);
    }
}