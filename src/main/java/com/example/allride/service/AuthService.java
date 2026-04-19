package com.example.allride.service;

import com.example.allride.dto.request.LoginRequest;
import com.example.allride.dto.response.LoginResponse;
import com.example.allride.entity.User;
import com.example.allride.repository.UserRepository;
import com.example.allride.dto.request.SignupRequest;
import com.example.allride.dto.response.SignupResponse;
import com.example.allride.security.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public SignupResponse signup(SignupRequest request) {

        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            // return "Email already exists!";
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .role(request.getRole())
                .build();

        userRepository.save(user);

        return SignupResponse.builder()
                .email(user.getEmail())
                .fullName(user.getFullName())
                .message("User registered successfully")
                .build();
    }

    public LoginResponse login(LoginRequest request) {

//        UsernamePasswordAuthenticationToken authRequestToken = new UsernamePasswordAuthenticationToken(
//                                                               request.getEmail(),
//                                                               request.getPassword()
//                                                              );
//
//        Authentication authentication = authenticationManager.authenticate(authRequestToken);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

//        Replaced and Handled by Spring Authentication Manager
//        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//            throw new BadCredentialsException("Invalid email or password");
//        }

        return LoginResponse.builder()
                .fullName(user.getFullName())
                .message("Login successful! Welcome "+user.getFullName())
                .token(jwtService.generateToken(user.getEmail()))
                .build();
    }
}