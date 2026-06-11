package com.assetflow.backend.dto.asset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssetResponse {

    private Long id;

    private String name;

    private String category;

    private String status;

    private LocalDate purchaseDate;

}