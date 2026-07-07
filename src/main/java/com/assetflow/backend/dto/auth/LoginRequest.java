package com.assetflow.backend.dto.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Username is required")
    @Size(max = 40, message = "Username must be between 3 and 40 characters")
    @Pattern(regexp = "^\\s*$|.{3,}", message = "Username must be between 3 and 40 characters")
    @Pattern(regexp = "^\\s*$|[A-Za-z0-9_-]+$", message = "Username can only contain letters, numbers, underscores, and hyphens")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(max = 72, message = "Password must be between 6 and 72 characters")
    @Pattern(regexp = "^\\s*$|.{6,}", message = "Password must be between 6 and 72 characters")
    private String password;
}
