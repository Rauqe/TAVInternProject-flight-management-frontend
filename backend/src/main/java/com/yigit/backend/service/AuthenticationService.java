package com.yigit.backend.service;

import com.yigit.backend.Role;
import com.yigit.backend.User;
import com.yigit.backend.UserRepository;
import com.yigit.backend.dto.AuthenticationRequest;
import com.yigit.backend.dto.AuthenticationResponse;
import com.yigit.backend.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : Role.USER)
                .build();
        repository.save(user);
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .expiresIn(jwtService.extractExpiration(jwtToken).getTime())
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        // If the above line does not throw an exception, the user is authenticated
        var user = repository.findByUsername(request.getUsername())
                .orElseThrow(); // Should not happen if authentication is successful
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .role(user.getRole())
                .expiresIn(jwtService.extractExpiration(jwtToken).getTime())
                .build();
    }
} 