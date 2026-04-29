package com.example.allride.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignupResponse {
    private String email;
    private String fullName;
    private String message;
}
