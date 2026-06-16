package com.assetflow.backend.service;

import com.assetflow.backend.dto.auth.LoginRequest;
import com.assetflow.backend.dto.auth.RegisterRequest;
import com.assetflow.backend.exception.DuplicateUsernameException;
import com.assetflow.backend.exception.InvalidCredentialsException;
import com.assetflow.backend.model.Role;
import com.assetflow.backend.model.User;
import com.assetflow.backend.repository.UserRepository;
import com.assetflow.backend.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @Test
    void registerWithDuplicateUsernameThrowsException() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("talha");
        request.setPassword("secret123");

        when(userRepository.existsByUsername("talha")).thenReturn(true);

        assertThrows(DuplicateUsernameException.class, () -> authService.register(request));

        verify(userRepository, never()).save(any());
    }

    @Test
    void registerWithNewUsernameEncodesPasswordAndSavesUser() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("talha");
        request.setPassword("secret123");

        when(userRepository.existsByUsername("talha")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("encoded-password");

        authService.register(request);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("talha", savedUser.getUsername());
        assertEquals("encoded-password", savedUser.getPassword());
        assertEquals(Role.USER, savedUser.getRole());
    }

    @Test
    void loginWithUnknownUsernameThrowsInvalidCredentialsException() {
        LoginRequest request = new LoginRequest();
        request.setUsername("talha");
        request.setPassword("secret123");

        when(userRepository.findByUsername("talha")).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));

        verify(passwordEncoder, never()).matches(any(), any());
        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void loginWithWrongPasswordThrowsInvalidCredentialsException() {
        LoginRequest request = new LoginRequest();
        request.setUsername("talha");
        request.setPassword("wrong-password");

        User user = new User();
        user.setUsername("talha");
        user.setPassword("encoded-password");
        user.setRole(Role.USER);

        when(userRepository.findByUsername("talha")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong-password", "encoded-password")).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authService.login(request));

        verify(jwtService, never()).generateToken(any());
    }

    @Test
    void loginWithValidCredentialsReturnsJwtToken() {
        LoginRequest request = new LoginRequest();
        request.setUsername("talha");
        request.setPassword("secret123");

        User user = new User();
        user.setUsername("talha");
        user.setPassword("encoded-password");
        user.setRole(Role.USER);

        when(userRepository.findByUsername("talha")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("secret123", "encoded-password")).thenReturn(true);
        when(jwtService.generateToken(user)).thenReturn("jwt-token");

        String token = authService.login(request);

        assertEquals("jwt-token", token);
    }
}