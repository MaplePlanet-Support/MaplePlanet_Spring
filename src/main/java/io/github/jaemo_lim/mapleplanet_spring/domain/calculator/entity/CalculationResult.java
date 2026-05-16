package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.entity;

import io.github.jaemo_lim.mapleplanet_spring.domain.character.entity.Character;
import io.github.jaemo_lim.mapleplanet_spring.global.enums.CalculationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "calculation_result")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CalculationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 프로필 없이 계산만 할 경우 null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id")
    private Character character;

    // 공유 링크용 UUID (/share/{shareKey})
    @Column(nullable = false, unique = true, length = 36)
    private String shareKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CalculationType calculationType;

    // 확률 테이블 변경 후에도 당시 입력값 보존
    @Column(nullable = false, columnDefinition = "TEXT")
    private String inputSnapshot;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String outputSnapshot;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
