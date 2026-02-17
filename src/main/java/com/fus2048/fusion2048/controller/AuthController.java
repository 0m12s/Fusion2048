package com.fus2048.fusion2048.controller;

import com.fus2048.fusion2048.dto.AuthResponse;
import com.fus2048.fusion2048.dto.LoginRequest;
import com.fus2048.fusion2048.dto.RegisterRequest;
import com.fus2048.fusion2048.security.JwtUtil;
import com.fus2048.fusion2048.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    // REGISTER
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        String msg = authService.register(request.getUsername(), request.getPassword());

        return ResponseEntity.ok(new AuthResponse(msg, null));
    }


    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        String token = jwtUtil.generateToken(request.getUsername());

        return ResponseEntity.ok(new AuthResponse("Login successful", token));
    }

}

