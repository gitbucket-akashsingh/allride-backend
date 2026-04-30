package com.example.allride.auth.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@Builder
public class SessionResponse {

    private Long id;
    private String device;
    private String ip;
    private LocalDateTime lastUsedAt;
    private boolean current;

}
