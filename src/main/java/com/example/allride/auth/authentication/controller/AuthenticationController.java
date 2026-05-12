package com.example.allride.auth.authentication.controller;

import com.example.allride.auth.authentication.dto.request.LoginRequest;
import com.example.allride.auth.authentication.dto.request.SignupRequest;
import com.example.allride.auth.authentication.dto.response.LoginResponse;
import com.example.allride.auth.authentication.dto.response.SignupResponse;
import com.example.allride.auth.authentication.dto.request.ClientInfo;
import com.example.allride.auth.authentication.dto.request.RefreshRequest;
import com.example.allride.auth.authentication.dto.response.CurrentUserResponse;
import com.example.allride.auth.authentication.service.AuthenticationService;
import com.example.allride.auth.common.util.DeviceUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication APIs",
        description = "Handles authentication and user identity"
)
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Operation(
            summary = "Register new user",
            description = "Creates a new user account"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping("/signup")
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest request) {

        log.info("Signup request received for email: {}", request.getEmail());

        SignupResponse response= authenticationService.signup(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Operation(
            summary = "Login user",
            description = "Authenticates user and returns access + refresh tokens"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request,
                                               HttpServletRequest httpServletRequest) {

        ClientInfo info = ClientInfo.builder()
                .ip(httpServletRequest.getRemoteAddr())
                .device(DeviceUtil.extractDevice(httpServletRequest))
                .userAgent(httpServletRequest.getHeader("User-Agent"))
                .build();

        log.info(
                "Login request received from IP: {}, device: {}",
                info.getIp(),
                info.getDevice()
        );

        LoginResponse response= authenticationService.login(request, info);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Get current logged-in user",
            description = "Returns currently authenticated user details"
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User fetched successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me")
    public ResponseEntity<CurrentUserResponse> me(Authentication authentication) {
        return ResponseEntity.ok(authenticationService.getCurrentUser(authentication));
    }

    @Operation(
            summary = "Refresh access token",
            description = "Generates a new access token using refresh token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody RefreshRequest request) {

        return ResponseEntity.ok(authenticationService.refresh(request));
    }
}
