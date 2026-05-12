package com.example.allride.auth.session.controller;

import com.example.allride.auth.authentication.entity.User;
import com.example.allride.auth.authentication.dto.request.RefreshRequest;
import com.example.allride.auth.session.dto.SessionResponse;
import com.example.allride.auth.session.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(
        name = "Authentication APIs",
        description = "Handles authentication, JWT tokens, sessions and user identity"
)
public class SessionController {

    private final SessionService sessionService;

    @Operation(
            summary = "Logout user",
            description = "Revokes refresh token and logs user out"
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Logged out successfully")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshRequest request) {

        sessionService.logout(request.getRefreshToken());
        log.info("User logged out");
        return ResponseEntity.ok("Logged out");
    }


    @Operation(
            summary = "Get active sessions",
            description = "Returns all active logged-in sessions/devices"
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Sessions fetched successfully")
    })
    @GetMapping("/sessions")
    public List<SessionResponse> getSessions(@AuthenticationPrincipal User user, @RequestHeader("X-Session-Id") Long currentSessionId) {

        return sessionService.getSessions(user, currentSessionId);
    }

    @Operation(
            summary = "Logout specific device",
            description = "Revokes a specific active session/device"
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Session revoked"),
            @ApiResponse(responseCode = "404", description = "Session not found")
    })
    @DeleteMapping("/sessions/{id}")
    public ResponseEntity<?> logoutDevice(@PathVariable Long id, @AuthenticationPrincipal User user
    ) {
        sessionService.logoutDevice(user, id);
        return ResponseEntity.ok("Session revoked");
    }

    @Operation(
            summary = "Logout from all devices",
            description = "Revokes all active sessions for current user"
    )
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All sessions revoked")
    })
    @DeleteMapping("/sessions/all")
    public ResponseEntity<?> logoutAll(
            @AuthenticationPrincipal User user
    ) {
        sessionService.logoutAll(user);
        return ResponseEntity.ok("All sessions revoked");
    }
}
