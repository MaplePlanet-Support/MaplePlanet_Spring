package io.github.jaemo_lim.mapleplanet_spring.domain.skill.entity;

import io.github.jaemo_lim.mapleplanet_spring.domain.job.entity.Job;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "skill")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int maxLevel;

    // V2.0 추가 예정: damagePercent, maxHits
}
