package com.assetflow.backend.dto.asset;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AssetUpdateRequest {

    private String name;
    private String category;
    private String status;
    private LocalDate purchaseDate;
}