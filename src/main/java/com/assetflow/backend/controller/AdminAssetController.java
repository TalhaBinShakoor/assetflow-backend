package com.assetflow.backend.controller;

import com.assetflow.backend.dto.asset.AdminAssetResponse;
import com.assetflow.backend.service.AssetService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/assets")
public class AdminAssetController {

    private final AssetService assetService;

    public AdminAssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AdminAssetResponse> getAllAssetsForAdmin() {
        return assetService.getAllAssetsForAdmin();
    }
}
