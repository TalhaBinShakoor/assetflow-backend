package com.assetflow.backend.mapper;

import com.assetflow.backend.dto.asset.AssetCreateRequest;
import com.assetflow.backend.dto.asset.AssetResponse;
import com.assetflow.backend.dto.asset.AssetUpdateRequest;
import com.assetflow.backend.model.Asset;

public class AssetMapper {

    // Request → Entity (CREATE)
    public static Asset toEntity(AssetCreateRequest request) {
        Asset asset = new Asset();

        asset.setName(request.getName());
        asset.setCategory(request.getCategory());
        asset.setStatus(request.getStatus());
        asset.setPurchaseDate(request.getPurchaseDate());

        return asset;
    }

    // Request → Entity (UPDATE)
    public static void updateEntity(Asset asset, AssetUpdateRequest request) {
        asset.setName(request.getName());
        asset.setCategory(request.getCategory());
        asset.setStatus(request.getStatus());
        asset.setPurchaseDate(request.getPurchaseDate());
    }

    // Entity → Response (used everywhere)
    public static AssetResponse toResponse(Asset asset) {
        return AssetResponse.builder()
                .id(asset.getId())
                .name(asset.getName())
                .category(asset.getCategory())
                .status(asset.getStatus())
                .purchaseDate(asset.getPurchaseDate())
                .build();
    }
}