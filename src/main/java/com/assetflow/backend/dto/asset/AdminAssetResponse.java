package com.assetflow.backend.dto.asset;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class AdminAssetResponse {

    private Long id;

    private String name;

    private String category;

    private String status;

    private LocalDate purchaseDate;

    private String ownerUsername;

}