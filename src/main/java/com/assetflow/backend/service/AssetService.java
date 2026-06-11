package com.assetflow.backend.service;

import com.assetflow.backend.model.Asset;
import com.assetflow.backend.repository.AssetRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    public AssetService(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    public Optional<Asset> getAssetById(Long id) {
        return assetRepository.findById(id);
    }

    public Asset createAsset(Asset asset) {
        return assetRepository.save(asset);
    }

    public Asset updateAsset(Long id, Asset updatedAsset) {
        Asset existing = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found with id: " + id));

        existing.setName(updatedAsset.getName());
        existing.setCategory(updatedAsset.getCategory());
        existing.setStatus(updatedAsset.getStatus());
        existing.setPurchaseDate(updatedAsset.getPurchaseDate());

        return assetRepository.save(existing);
    }

    public void deleteAsset(Long id) {
        Asset existing = assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset not found with id: " + id));

        assetRepository.delete(existing);
    }
}