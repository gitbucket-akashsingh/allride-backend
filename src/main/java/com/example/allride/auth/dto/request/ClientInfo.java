package com.example.allride.auth.dto.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientInfo {
    private String ip;
    private String device;
    private String userAgent;
}
