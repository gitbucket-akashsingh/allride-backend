package com.example.allride.auth.session.entity;

import com.example.allride.auth.authentication.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name="user_sessions")
public class UserSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String deviceName;

    private String ipAddress;

    @Column(columnDefinition = "TEXT")
    private String userAgent;

    private LocalDateTime createdAt;

    private LocalDateTime lastUsedAt;

    private boolean revoked = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


}
