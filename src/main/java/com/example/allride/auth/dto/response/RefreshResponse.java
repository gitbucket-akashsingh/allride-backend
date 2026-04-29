package com.example.allride.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshResponse {

    private String accessToken;
    private long expiresIn;
}
