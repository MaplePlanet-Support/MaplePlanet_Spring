package io.github.jaemo_lim.mapleplanet_spring.domain.simulator.cube.entity;

import io.github.jaemo_lim.mapleplanet_spring.global.enums.ItemGrade;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cube_grade_probability")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CubeGradeProbability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String cubeType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ItemGrade currentGrade;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ItemGrade targetGrade;

    @Column(nullable = false, precision = 6, scale = 5)
    private BigDecimal probability;
}
