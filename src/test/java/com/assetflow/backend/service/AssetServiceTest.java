package com.assetflow.backend.service;

import com.assetflow.backend.dto.asset.AssetUpdateRequest;
import java.time.LocalDate;
import com.assetflow.backend.dto.asset.AssetResponse;
import com.assetflow.backend.dto.asset.AdminAssetResponse;
import com.assetflow.backend.exception.AssetNotFoundException;
import com.assetflow.backend.model.Asset;
import com.assetflow.backend.model.Role;
import com.assetflow.backend.model.User;
import com.assetflow.backend.repository.AssetRepository;
import com.assetflow.backend.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AssetService assetService;

    @AfterEach
    void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getAssetsForUserReturnsOnlyCurrentUsersAssets() {
        setAuthenticatedUsername("talha");

        User user = user(1L, "talha", Role.USER);
        Asset laptop = asset(10L, "Laptop", user);

        when(userRepository.findByUsername("talha")).thenReturn(Optional.of(user));
        when(assetRepository.findByUser(user)).thenReturn(List.of(laptop));

        List<AssetResponse> assets = assetService.getAssetsForUser();

        assertEquals(1, assets.size());
        assertEquals(10L, assets.getFirst().getId());
        assertEquals("Laptop", assets.getFirst().getName());

        verify(assetRepository).findByUser(user);
    }

    @Test
    void getAssetByIdReturnsAssetWhenUserOwnsIt() {
        setAuthenticatedUsername("talha");

        User user = user(1L, "talha", Role.USER);
        Asset laptop = asset(10L, "Laptop", user);

        when(userRepository.findByUsername("talha")).thenReturn(Optional.of(user));
        when(assetRepository.findById(10L)).thenReturn(Optional.of(laptop));

        AssetResponse response = assetService.getAssetById(10L);

        assertEquals(10L, response.getId());
        assertEquals("Laptop", response.getName());
    }

    @Test
    void getAssetByIdThrowsNotFoundWhenUserDoesNotOwnIt() {
        setAuthenticatedUsername("talha");

        User currentUser = user(1L, "talha", Role.USER);
        User otherUser = user(2L, "sara", Role.USER);
        Asset otherUsersAsset = asset(10L, "Monitor", otherUser);

        when(userRepository.findByUsername("talha")).thenReturn(Optional.of(currentUser));
        when(assetRepository.findById(10L)).thenReturn(Optional.of(otherUsersAsset));

        assertThrows(AssetNotFoundException.class, () -> assetService.getAssetById(10L));
    }

    @Test
    void updateAssetThrowsNotFoundWhenUserDoesNotOwnIt() {
        setAuthenticatedUsername("talha");

        User currentUser = user(1L, "talha", Role.USER);
        User otherUser = user(2L, "sara", Role.USER);
        Asset otherUsersAsset = asset(10L, "Monitor", otherUser);

        AssetUpdateRequest request = new AssetUpdateRequest();
        request.setName("Monitor Pro");
        request.setCategory("Electronics");
        request.setStatus("IN_REPAIR");
        request.setPurchaseDate(LocalDate.of(2026, 6, 28));

        when(userRepository.findByUsername("talha")).thenReturn(Optional.of(currentUser));
        when(assetRepository.findById(10L)).thenReturn(Optional.of(otherUsersAsset));

        assertThrows(AssetNotFoundException.class, () -> assetService.updateAsset(10L, request));
    }

    @Test
    void deleteAssetThrowsNotFoundWhenUserDoesNotOwnIt() {
        setAuthenticatedUsername("talha");

        User currentUser = user(1L, "talha", Role.USER);
        User otherUser = user(2L, "sara", Role.USER);
        Asset otherUsersAsset = asset(10L, "Monitor", otherUser);

        when(userRepository.findByUsername("talha")).thenReturn(Optional.of(currentUser));
        when(assetRepository.findById(10L)).thenReturn(Optional.of(otherUsersAsset));

        assertThrows(AssetNotFoundException.class, () -> assetService.deleteAsset(10L));
    }

    private void setAuthenticatedUsername(String username) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(username, null)
        );
    }

    private User user(Long id, String username, Role role) {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setRole(role);
        return user;
    }

    private Asset asset(Long id, String name, User user) {
        Asset asset = new Asset();
        asset.setId(id);
        asset.setName(name);
        asset.setCategory("Electronics");
        asset.setStatus("ACTIVE");
        asset.setUser(user);
        return asset;
    }

    @Test
    void getAssetByIdReturnsAssetWhenCurrentUserIsAdmin() {
        setAuthenticatedUsername("admin");

        User admin = user(1L, "admin", Role.ADMIN);
        User owner = user(2L, "sara", Role.USER);
        Asset otherUsersAsset = asset(10L, "Monitor", owner);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(assetRepository.findById(10L)).thenReturn(Optional.of(otherUsersAsset));

        AssetResponse response = assetService.getAssetById(10L);

        assertEquals(10L, response.getId());
        assertEquals("Monitor", response.getName());
    }

    @Test
    void updateAssetAllowsAdminToUpdateAnyUsersAsset() {
        setAuthenticatedUsername("admin");

        User admin = user(1L, "admin", Role.ADMIN);
        User owner = user(2L, "sara", Role.USER);
        Asset otherUsersAsset = asset(10L, "Monitor", owner);

        AssetUpdateRequest request = new AssetUpdateRequest();
        request.setName("Monitor Pro");
        request.setCategory("Electronics");
        request.setStatus("IN_REPAIR");
        request.setPurchaseDate(LocalDate.of(2026, 6, 28));

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(assetRepository.findById(10L)).thenReturn(Optional.of(otherUsersAsset));
        when(assetRepository.save(otherUsersAsset)).thenReturn(otherUsersAsset);

        AssetResponse response = assetService.updateAsset(10L, request);

        assertEquals(10L, response.getId());
        assertEquals("Monitor Pro", response.getName());
        assertEquals("Electronics", response.getCategory());
        assertEquals("IN_REPAIR", response.getStatus());

        verify(assetRepository).save(otherUsersAsset);
    }

    @Test
    void deleteAssetAllowsAdminToDeleteAnyUsersAsset() {
        setAuthenticatedUsername("admin");

        User admin = user(1L, "admin", Role.ADMIN);
        User owner = user(2L, "sara", Role.USER);
        Asset otherUsersAsset = asset(10L, "Monitor", owner);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(admin));
        when(assetRepository.findById(10L)).thenReturn(Optional.of(otherUsersAsset));

        assetService.deleteAsset(10L);

        verify(assetRepository).delete(otherUsersAsset);
    }

    @Test
    void getAllAssetsForAdminReturnsAllAssets() {
        User talha = user(1L, "talha", Role.USER);
        User sara = user(2L, "sara", Role.USER);

        Asset laptop = asset(10L, "Laptop", talha);
        Asset monitor = asset(20L, "Monitor", sara);

        when(assetRepository.findAll()).thenReturn(List.of(laptop, monitor));

        List<AdminAssetResponse> assets = assetService.getAllAssetsForAdmin();

        assertEquals(2, assets.size());
        assertEquals("Laptop", assets.get(0).getName());
        assertEquals("Monitor", assets.get(1).getName());
        assertEquals("talha", assets.get(0).getOwnerUsername());
        assertEquals("sara", assets.get(1).getOwnerUsername());

        verify(assetRepository).findAll();
    }
}