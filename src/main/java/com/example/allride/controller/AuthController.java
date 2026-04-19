package com.example.allride.controller;

import com.example.allride.dto.request.LoginRequest;
import com.example.allride.dto.request.SignupRequest;
import com.example.allride.dto.response.LoginResponse;
import com.example.allride.entity.User;
import com.example.allride.dto.response.SignupResponse;
import com.example.allride.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {
        System.out.println("signup called");
        SignupResponse response= authService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response= authService.login(request);
        return ResponseEntity.ok(response);
    }
}