package com.example.allride.auth.authentication.dto.request;

import com.example.allride.auth.common.enums.Role;
import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private Role role;
}
