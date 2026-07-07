package com.assetflow.backend.controller;

import com.assetflow.backend.exception.DuplicateUsernameException;
import com.assetflow.backend.exception.InvalidCredentialsException;
import com.assetflow.backend.handler.GlobalExceptionHandler;
import com.assetflow.backend.security.JwtService;
import com.assetflow.backend.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void registerWithBlankUsernameReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "username": "",
                  "password": "secret123"
                }
                """)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400)).andExpect(jsonPath("$.message").value("Validation failed")).andExpect(jsonPath("$.errors.username").value("Username is required"));
    }

    @Test
    void loginWithBlankPasswordReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "username": "talha",
                  "password": ""
                }
                """)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400)).andExpect(jsonPath("$.message").value("Validation failed")).andExpect(jsonPath("$.errors.password").value("Password is required"));
    }

    @Test
    void registerWithDuplicateUsernameReturnsConflict() throws Exception {
        doThrow(new DuplicateUsernameException("talha")).when(authService).register(any());

        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "username": "talha",
                  "password": "secret123"
                }
                """)).andExpect(status().isConflict()).andExpect(jsonPath("$.status").value(409)).andExpect(jsonPath("$.message").value("Username already exists: talha"));
    }

    @Test
    void loginWithInvalidCredentialsReturnsUnauthorized() throws Exception {
        when(authService.login(any())).thenThrow(new InvalidCredentialsException());

        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "username": "talha",
                  "password": "wrong-password"
                }
                """)).andExpect(status().isUnauthorized()).andExpect(jsonPath("$.status").value(401)).andExpect(jsonPath("$.message").value("Invalid username or password"));
    }

    @Test
    void loginWithValidCredentialsReturnsToken() throws Exception {
        when(authService.login(any())).thenReturn("jwt-token");

        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "username": "talha",
                  "password": "secret123"
                }
                """)).andExpect(status().isOk()).andExpect(content().string("jwt-token"));
    }

    @Test
    void registerWithBlankPasswordReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "username": "talha",
                  "password": ""
                }
                """)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400)).andExpect(jsonPath("$.message").value("Validation failed")).andExpect(jsonPath("$.errors.password").value("Password is required"));

        verify(authService, never()).register(any());
    }

    @Test
    void registerWithShortUsernameReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "username": "ab",
                  "password": "secret123"
                }
                """)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400)).andExpect(jsonPath("$.message").value("Validation failed")).andExpect(jsonPath("$.errors.username").value("Username must be between 3 and 40 characters"));

        verify(authService, never()).register(any());
    }

    @Test
    void registerWithInvalidUsernameCharactersReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "username": "talha!",
                  "password": "secret123"
                }
                """)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400)).andExpect(jsonPath("$.message").value("Validation failed")).andExpect(jsonPath("$.errors.username").value("Username can only contain letters, numbers, underscores, and hyphens"));

        verify(authService, never()).register(any());
    }

    @Test
    void loginWithBlankUsernameReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "username": "",
                  "password": "secret123"
                }
                """)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400)).andExpect(jsonPath("$.message").value("Validation failed")).andExpect(jsonPath("$.errors.username").value("Username is required"));

        verify(authService, never()).login(any());
    }

    @Test
    void registerWithInvalidJsonReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "username": "talha",
                  "password":
                }
                """)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400)).andExpect(jsonPath("$.message").value("Invalid request body or invalid field format"));

        verify(authService, never()).register(any());
    }

    @Test
    void registerWithShortPasswordReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "username": "talha",
                  "password": "123"
                }
                """)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400)).andExpect(jsonPath("$.message").value("Validation failed")).andExpect(jsonPath("$.errors.password").value("Password must be between 6 and 72 characters"));

        verify(authService, never()).register(any());
    }

    @Test
    void registerWithTooLongPasswordReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/auth/register").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "username": "talha",
                  "password": "%s"
                }
                """.formatted("a".repeat(73)))).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400)).andExpect(jsonPath("$.message").value("Validation failed")).andExpect(jsonPath("$.errors.password").value("Password must be between 6 and 72 characters"));

        verify(authService, never()).register(any());
    }

    @Test
    void loginWithTooLongUsernameReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "username": "%s",
                  "password": "secret123"
                }
                """.formatted("a".repeat(41)))).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400)).andExpect(jsonPath("$.message").value("Validation failed")).andExpect(jsonPath("$.errors.username").value("Username must be between 3 and 40 characters"));

        verify(authService, never()).login(any());
    }

    @Test
    void loginWithShortPasswordReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content("""
                {
                  "username": "talha",
                  "password": "123"
                }
                """)).andExpect(status().isBadRequest()).andExpect(jsonPath("$.status").value(400)).andExpect(jsonPath("$.message").value("Validation failed")).andExpect(jsonPath("$.errors.password").value("Password must be between 6 and 72 characters"));

        verify(authService, never()).login(any());
    }
}
