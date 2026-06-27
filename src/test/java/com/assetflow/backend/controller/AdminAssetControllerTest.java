package com.assetflow.backend.controller;

import com.assetflow.backend.dto.asset.AdminAssetResponse;
import com.assetflow.backend.security.JwtService;
import com.assetflow.backend.service.AssetService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AdminAssetController.class)
@Import(AdminAssetControllerTest.MethodSecurityTestConfig.class)
class AdminAssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssetService assetService;

    @MockitoBean
    private JwtService jwtService;

    @TestConfiguration
    @EnableMethodSecurity
    static class MethodSecurityTestConfig {
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminCanGetAllAssets() throws Exception {
        AdminAssetResponse laptop = AdminAssetResponse.builder()
                .id(1L)
                .name("Laptop")
                .category("Electronics")
                .status("ACTIVE")
                .purchaseDate(LocalDate.of(2025, 1, 10))
                .ownerUsername("talha")
                .build();

        when(assetService.getAllAssetsForAdmin()).thenReturn(List.of(laptop));

        mockMvc.perform(get("/api/admin/assets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].ownerUsername").value("talha"));

        verify(assetService).getAllAssetsForAdmin();
    }

    @Test
    @WithMockUser(roles = "USER")
    void userCannotGetAllAssetsFromAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/admin/assets"))
                .andExpect(status().isForbidden());

        verify(assetService, never()).getAllAssetsForAdmin();
    }

    @Test
    void unauthenticatedUserCannotGetAllAssetsFromAdminEndpoint() throws Exception {
        mockMvc.perform(get("/api/admin/assets"))
                .andExpect(status().isUnauthorized());

        verify(assetService, never()).getAllAssetsForAdmin();
    }

}