package com.example.allride.auth.session.service;

import com.example.allride.auth.authentication.entity.User;
import com.example.allride.auth.authorization.jwt.JwtService;
import com.example.allride.auth.authentication.dto.request.RefreshRequest;
import com.example.allride.auth.authentication.dto.response.RefreshResponse;
import com.example.allride.auth.session.dto.SessionResponse;
import com.example.allride.auth.session.entity.RefreshToken;
import com.example.allride.auth.session.entity.UserSession;
import com.example.allride.auth.session.repository.RefreshTokenRepository;
import com.example.allride.auth.session.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserSessionRepository userSessionRepository;

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
                .accessToken(newAccessToken)
                .expiresIn(900000)
                .build();
    }

    //    LOGOUT Service
    public void logout(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow();

        refreshToken.setRevoked(true);
        refreshTokenRepository.save(refreshToken);
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
