package com.server.app.controllers;

import com.server.app.dto.auth.AuthResponse;
import com.server.app.dto.auth.LoginDto;
import com.server.app.dto.auth.UpdatePasswordDto;
import com.server.app.dto.auth.UpdateProfileDto;
import com.server.app.dto.user.UserCreateDto;
import com.server.app.entities.User;
import com.server.app.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginDto dto
    ) {
        return ResponseEntity.ok(userService.login(dto));
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(
            @Valid @RequestBody UserCreateDto dto
    ) {
        return ResponseEntity.ok(userService.signUp(dto));
    }

    @GetMapping("/profile")
    public ResponseEntity<User> profile(
            Authentication authentication
    ) {
        User authenticatedUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                userService.findById(authenticatedUser.getId())
        );
    }

    @PutMapping("/update/profile")
    public ResponseEntity<AuthResponse> updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileDto dto
    ) {
        User authenticatedUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                userService.updateProfile(
                        authenticatedUser.getId(),
                        dto
                )
        );
    }

    @PutMapping("/update/password")
    public ResponseEntity<User> updatePassword(
            Authentication authentication,
            @Valid @RequestBody UpdatePasswordDto dto
    ) {
        User authenticatedUser = (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                userService.updatePassword(
                        authenticatedUser.getId(),
                        dto
                )
        );
    }
}