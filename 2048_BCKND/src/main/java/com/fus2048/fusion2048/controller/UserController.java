package com.fus2048.fusion2048.controller;

import com.fus2048.fusion2048.dto.UserResponse;
import com.fus2048.fusion2048.entity.User;
import com.fus2048.fusion2048.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public UserResponse getCurrentUser() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(user.getUsername(), user.getBestScore());
    }
}
