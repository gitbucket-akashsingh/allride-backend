package com.example.allride.auth.session.repository;

import com.example.allride.auth.session.entity.UserSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long >{

    List<UserSession> findByUserIdAndRevokedFalse(Long userId);

    Optional<UserSession> findByIdAndUserId(Long sessionId, Long userId);

    @Modifying
    @Query("update UserSession s set s.revoked = true where s.user.id = :userId")
    void revokeAllByUserId(@Param("userId") Long userId);


}
