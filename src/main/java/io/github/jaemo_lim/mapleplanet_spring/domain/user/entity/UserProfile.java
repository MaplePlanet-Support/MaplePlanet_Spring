package io.github.jaemo_lim.mapleplanet_spring.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_profile")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 유저가 직접 설정하는 닉네임 또는 ID
    @Column(nullable = false, unique = true, length = 30)
    private String identifier;

    // 단순 보안용 PIN (BCrypt 해시). 로그인 기능 도입 전 임시 접근 제어
    @Column(nullable = false, length = 60)
    private String pinHash;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
