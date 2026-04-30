package com.example.allride.auth.controller;

import com.example.allride.auth.dto.request.*;
import com.example.allride.auth.dto.response.CurrentUserResponse;
import com.example.allride.auth.dto.response.LoginResponse;
import com.example.allride.auth.dto.response.SessionResponse;
import com.example.allride.auth.dto.response.SignupResponse;
import com.example.allride.auth.entity.User;
import com.example.allride.auth.service.AuthService;
import com.example.allride.auth.util.DeviceUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request,
                                               HttpServletRequest httpServletRequest) {

        ClientInfo info = ClientInfo.builder()
                .ip(httpServletRequest.getRemoteAddr())
                .device(DeviceUtil.extractDevice(httpServletRequest))
                .userAgent(httpServletRequest.getHeader("User-Agent"))
                .build();

        LoginResponse response= authService.login(request, info);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> me(Authentication authentication) {
        return ResponseEntity.ok(authService.getCurrentUser(authentication));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {

        return ResponseEntity.ok(authService.refresh(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshRequest request) {

        authService.logout(request.getRefreshToken());
        return ResponseEntity.ok("Logged out");
    }

    @GetMapping("/sessions")
    public List<SessionResponse> getSessions(@AuthenticationPrincipal User user, @RequestHeader("X-Session-Id") Long currentSessionId) {

        return authService.getSessions(user, currentSessionId);
    }

    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<?> logoutDevice(@PathVariable Long id, @AuthenticationPrincipal User user
    ) {
        authService.logoutDevice(user, id);
        return ResponseEntity.ok("Session revoked");
    }

    @DeleteMapping("/sessions/all")
    public ResponseEntity<?> logoutAll(
            @AuthenticationPrincipal User user
    ) {
        authService.logoutAll(user);
        return ResponseEntity.ok("All sessions revoked");
    }


}