package com.assetflow.backend.repository;

import com.assetflow.backend.model.Asset;
import com.assetflow.backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    List<Asset> findByUser(User user);
}