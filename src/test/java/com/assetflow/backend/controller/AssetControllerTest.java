package com.assetflow.backend.controller;

import com.assetflow.backend.handler.GlobalExceptionHandler;
import com.assetflow.backend.security.JwtService;
import com.assetflow.backend.service.AssetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AssetController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AssetService assetService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void createAssetWithTooLongNameReturnsBadRequest() throws Exception {
        String tooLongName = "a".repeat(121);

        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "%s",
                                  "category": "Laptop",
                                  "status": "Active",
                                  "purchaseDate": "2026-06-20"
                                }
                                """.formatted(tooLongName)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.name").value("Name must be 120 characters or fewer"));

        verify(assetService, never()).createAsset(any());
    }

    @Test
    void updateAssetWithTooLongCategoryReturnsBadRequest() throws Exception {
        String tooLongCategory = "a".repeat(81);

        mockMvc.perform(put("/api/assets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "MacBook Pro",
                                  "category": "%s",
                                  "status": "Active",
                                  "purchaseDate": "2026-06-20"
                                }
                                """.formatted(tooLongCategory)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.category").value("Category must be 80 characters or fewer"));

        verify(assetService, never()).updateAsset(any(), any());
    }

    @Test
    void updateAssetWithTooLongStatusReturnsBadRequest() throws Exception {
        String tooLongStatus = "a".repeat(41);

        mockMvc.perform(put("/api/assets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "MacBook Pro",
                                  "category": "Laptop",
                                  "status": "%s",
                                  "purchaseDate": "2026-06-20"
                                }
                                """.formatted(tooLongStatus)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.status").value("Status must be 40 characters or fewer"));

        verify(assetService, never()).updateAsset(any(), any());
    }

    @Test
    void createAssetWithFuturePurchaseDateReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/assets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "MacBook Pro",
                                  "category": "Laptop",
                                  "status": "Active",
                                  "purchaseDate": "2999-01-01"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.purchaseDate").value("Purchase date cannot be in the future"));

        verify(assetService, never()).createAsset(any());
    }

    @Test
    void updateAssetWithFuturePurchaseDateReturnsBadRequest() throws Exception {
        mockMvc.perform(put("/api/assets/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "MacBook Pro",
                                  "category": "Laptop",
                                  "status": "Active",
                                  "purchaseDate": "2999-01-01"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.purchaseDate").value("Purchase date cannot be in the future"));

        verify(assetService, never()).updateAsset(any(), any());
    }
}
