package com.assetflow.backend.dto.asset;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class AssetCreateRequest {

    @NotBlank
    private String name;

    @NotBlank
    private String category;

    @NotBlank
    private String status;

    @NotNull
    private LocalDate purchaseDate;
}
