package com.example.allride.security;

import com.example.allride.entity.User;
import com.example.allride.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
/*
 To implement a custom UserDetailsService in Spring Security,
 you must create a class that implements the UserDetailsService interface
 and override its single method: loadUserByUsername(String username).

This service is a core component used by the DaoAuthenticationProvider to retrieve user-specific data
from a data source (like a database) during the authentication process.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(
                        List.of(new SimpleGrantedAuthority(
                                "ROLE_" + user.getRole().name()
                        ))
                )
                .build();
    }
}
