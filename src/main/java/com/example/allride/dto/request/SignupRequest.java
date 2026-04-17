package com.example.allride.dto.request;

import com.example.allride.entity.Role;
import lombok.Data;

@Data
public class SignupRequest {
    private String email;
    private String password;
    private String fullName;
    private String phone;
    private Role role;
}
