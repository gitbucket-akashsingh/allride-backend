package com.example.allride.auth.service;

import com.example.allride.auth.dto.request.*;
import com.example.allride.auth.dto.response.*;
import com.example.allride.auth.entity.User;
import com.example.allride.auth.entity.UserSession;
import com.example.allride.auth.repository.RefreshTokenRepository;
import com.example.allride.auth.repository.UserRepository;
import com.example.allride.auth.repository.UserSessionRepository;
import com.example.allride.common.security.jwt.JwtService;
import com.example.allride.auth.entity.RefreshToken;
import com.example.allride.auth.util.DeviceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserSessionRepository userSessionRepository;

//    SIGNUP Service
    public SignupResponse signup(SignupRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            // return "Email already exists!";
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .role(request.getRole())
                .build();

        userRepository.save(user);

        return SignupResponse.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .message("User registered successfully")
                .build();
    }

//    LOGIN Service
    public LoginResponse login(LoginRequest request, ClientInfo info) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));
//
        UserSession session = UserSession.builder()
                .user(user)
                .deviceName(info.getDevice())
                .ipAddress(info.getIp())
                .userAgent(info.getUserAgent())
                .createdAt(LocalDateTime.now())
                .lastUsedAt(LocalDateTime.now())
                .build();

        userSessionRepository.save(session);

        String accessToken = jwtService.generateAccessToken(user, session.getId());
        String refreshToken = jwtService.generateRefreshToken(user);

        refreshTokenRepository.save(RefreshToken.builder()
                        .token(refreshToken)
                        .expiryDate(new java.util.Date(System.currentTimeMillis() + 7L * 24 * 60 * 60 * 1000))
                        .revoked(false)
                        .user(user)
                        .build()
        );


        return LoginResponse.builder()
                .fullName(user.getFullName())
                .message("Login successful! Welcome "+user.getFullName())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(900000)
//                .token(jwtService.generateToken(user.getEmail()))  Not needed as access and refresh token used
                .build();
    }

//    CURRENT USER Service
    public CurrentUserResponse getCurrentUser(Authentication authentication) {

        String email = authentication.getName();
        User user= userRepository.findByEmail(email)
                .orElseThrow();

        return CurrentUserResponse.builder()
                        .id(user.getId())
                        .fullName(user.getFullName())
                        .phone(user.getPhone())
                        .email(user.getEmail())
                        .role(user.getRole().name())
                        .build();
    }

//    LOGOUT Service
    public void logout(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                        .orElseThrow();

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
    }

//    REFRESH TOKEN Service
    public RefreshResponse refresh(RefreshRequest request) {

        String token = request.getRefreshToken();

        RefreshToken savedToken = refreshTokenRepository
                .findByToken(token)
                .orElseThrow(() ->
                        new RuntimeException("Refresh token not found"));

        if (savedToken.isRevoked()) {
            throw new RuntimeException("Refresh token revoked");
        }

        if (savedToken.getExpiryDate().before(new java.util.Date())) {
            throw new RuntimeException("Refresh token expired");
        }

        UserSession session = savedToken.getSession();
        session.setLastUsedAt(LocalDateTime.now());
        userSessionRepository.save(session);

        User user = savedToken.getUser();
        String newAccessToken =
                jwtService.generateAccessToken(user,session.getId());

        return RefreshResponse.builder()
                .accessToken(request.getRefreshToken())
                .expiresIn(900000)
                .build();
    }

    public List<SessionResponse> getSessions(User user, Long currentSessionId) {

        return userSessionRepository.findByUserIdAndRevokedFalse(user.getId())
                .stream()
                .map(s -> SessionResponse.builder()
                        .id(s.getId())
                        .device(s.getDeviceName())
                        .ip(s.getIpAddress())
                        .lastUsedAt(s.getLastUsedAt())
                        .current(s.getId().equals(currentSessionId))
                        .build())
                .toList();
    }

    @Transactional
    public void logoutDevice(User user, Long sessionId) {

        UserSession session = userSessionRepository
                .findByIdAndUserId(sessionId, user.getId())
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setRevoked(true);
    }

    @Transactional
    public void logoutAll(User user) {
        userSessionRepository.revokeAllByUserId(user.getId());
    }
}