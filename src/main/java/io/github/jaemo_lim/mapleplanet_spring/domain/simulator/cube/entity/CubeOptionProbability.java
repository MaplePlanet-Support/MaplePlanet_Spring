package io.github.jaemo_lim.mapleplanet_spring.domain.simulator.cube.entity;

import io.github.jaemo_lim.mapleplanet_spring.domain.job.entity.Job;
import io.github.jaemo_lim.mapleplanet_spring.global.enums.ItemGrade;
import io.github.jaemo_lim.mapleplanet_spring.global.enums.ItemType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cube_option_probability")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CubeOptionProbability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String cubeType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ItemGrade itemGrade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ItemType itemType;

    // null = 직업 공용 옵션
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private Job job;

    // 1 / 2 / 3 라인
    @Column(nullable = false)
    private int lineNumber;

    @Column(nullable = false, length = 50)
    private String optionName;

    @Column(nullable = false, length = 30)
    private String optionValue;

    @Column(nullable = false, precision = 6, scale = 5)
    private BigDecimal probability;

    // 메이플 플래닛 잡옵 배제 여부
    @Column(nullable = false)
    private boolean junk;
}
