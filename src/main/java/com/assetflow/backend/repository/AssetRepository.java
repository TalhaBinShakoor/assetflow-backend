package com.assetflow.backend.repository;

import com.assetflow.backend.model.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {
}