package com.assetflow.backend.controller;

import com.assetflow.backend.model.Asset;
import com.assetflow.backend.service.AssetService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    public Asset create(@RequestBody Asset asset) {
        return assetService.createAsset(asset);
    }

    @GetMapping
    public List<Asset> getAll() {
        return assetService.getAllAssets();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asset> getById(@PathVariable Long id) {
        return assetService.getAssetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public Asset updateAsset(@PathVariable Long id, @RequestBody Asset asset) {
        return assetService.updateAsset(id, asset);
    }

    @DeleteMapping("/{id}")
    public void deleteAsset(@PathVariable Long id) {
        assetService.deleteAsset(id);
    }
}