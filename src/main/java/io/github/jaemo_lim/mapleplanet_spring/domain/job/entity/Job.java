package io.github.jaemo_lim.mapleplanet_spring.domain.job.entity;

import io.github.jaemo_lim.mapleplanet_spring.global.enums.StatType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private StatType mainStat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private StatType subStat;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private StatType attackStat;
}
