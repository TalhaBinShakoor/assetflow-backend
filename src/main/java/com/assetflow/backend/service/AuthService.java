package com.assetflow.backend.service;

import com.assetflow.backend.dto.auth.LoginRequest;
import com.assetflow.backend.dto.auth.RegisterRequest;
import com.assetflow.backend.exception.InvalidCredentialsException;
import com.assetflow.backend.model.Role;
import com.assetflow.backend.model.User;
import com.assetflow.backend.repository.UserRepository;
import com.assetflow.backend.security.JwtService;
import com.assetflow.backend.exception.DuplicateUsernameException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUsernameException(request.getUsername());
        }

        // 1. Create user entity
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        // 2. Save to DB
        userRepository.save(user);
    }

    public String login(LoginRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(InvalidCredentialsException::new);

        boolean passwordMatch = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatch) {
            throw new InvalidCredentialsException();
        }

        return jwtService.generateToken(user);
    }
}