package com.assetflow.backend.dto.asset;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Data
public class AssetCreateRequest {

    @NotBlank(message = "Name is required")
    @Size(max = 120, message = "Name must be 120 characters or fewer")
    private String name;

    @NotBlank(message = "Category is required")
    @Size(max = 80, message = "Category must be 80 characters or fewer")
    private String category;

    @NotBlank(message = "Status is required")
    @Size(max = 40, message = "Status must be 40 characters or fewer")
    private String status;

    @NotNull(message = "Purchase date is required")
    @PastOrPresent(message = "Purchase date cannot be in the future")
    private LocalDate purchaseDate;
}
