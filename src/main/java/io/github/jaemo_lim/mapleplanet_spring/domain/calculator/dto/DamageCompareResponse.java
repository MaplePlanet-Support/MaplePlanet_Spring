package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DamageCompareResponse {

    private double statWithItemA;
    private double statWithItemB;
    private double damageWithItemA;
    private double damageWithItemB;
    private double magicWithItemA;
    private double magicWithItemB;
    private double expectedDmgWithItemA;      // 보스 기준 (총뎀 + 보공)
    private double expectedDmgWithItemB;
    private double expectedNormalDmgWithItemA; // 일반 기준 (총뎀만)
    private double expectedNormalDmgWithItemB;
    private double statDifference;   // B - A
    private double damageDifference; // B - A
    private double expectedDmgDiffPct;
    private String betterItem;       // "A", "B", "SAME"
}
