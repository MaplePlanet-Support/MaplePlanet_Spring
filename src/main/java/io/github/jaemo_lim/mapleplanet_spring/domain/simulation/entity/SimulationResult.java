package io.github.jaemo_lim.mapleplanet_spring.domain.simulation.entity;

import io.github.jaemo_lim.mapleplanet_spring.domain.job.entity.Job;
import io.github.jaemo_lim.mapleplanet_spring.global.enums.SimulationType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "simulation_result")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SimulationResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 브라우저 UUID (localStorage 저장, 비로그인 사용자 식별)
    @Column(nullable = false, length = 36)
    private String sessionKey;

    // 공유 링크용 UUID (/share/{shareKey})
    @Column(nullable = false, unique = true, length = 36)
    private String shareKey;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SimulationType simulationType;

    // 환산기 사용 시 직업 정보 (시뮬레이터만 사용 시 null)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private Job job;

    // 당시 입력값 스냅샷 (확률 테이블 변경 후에도 결과 보존)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String inputSnapshot;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String outputSnapshot;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
