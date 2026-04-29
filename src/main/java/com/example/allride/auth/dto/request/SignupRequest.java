package com.example.allride.auth.dto.request;

import com.example.allride.auth.entity.Role;
import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private Role role;
}
