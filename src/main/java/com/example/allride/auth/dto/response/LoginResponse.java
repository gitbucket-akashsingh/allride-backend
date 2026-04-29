package com.example.allride.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private String fullName;
    private String message;
    private String token;

    private String accessToken;
    private String refreshToken;
    private long expiresIn;

}
