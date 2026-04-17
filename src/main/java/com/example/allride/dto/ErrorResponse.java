package com.example.allride.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
//@Getter
//@Setter
//@AllArgsConstructor
//@NoArgsConstructor
//@RequiredArgsConstructor
public class ErrorResponse {
    private String message;
    private int status;
    private LocalDateTime timestamp;
}
