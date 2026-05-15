package io.github.jaemo_lim.mapleplanet_spring.domain.simulator.maker.entity;

import io.github.jaemo_lim.mapleplanet_spring.global.enums.OptionTier;
import io.github.jaemo_lim.mapleplanet_spring.global.enums.StatType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "maker_result_option")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MakerResultOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private MakerRecipe recipe;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private OptionTier optionTier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private StatType statType;

    @Column(nullable = false)
    private int value;

    @Column(nullable = false, precision = 6, scale = 5)
    private BigDecimal baseProbability;

    // 촉진제 적용 시 확률 (null = 변동 없음)
    @Column(precision = 6, scale = 5)
    private BigDecimal catalystProbability;

    // 보석 적용 시 확률 (null = 변동 없음)
    @Column(precision = 6, scale = 5)
    private BigDecimal gemProbability;
}
