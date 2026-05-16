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
    private double statDifference;   // B - A
    private double damageDifference; // B - A
    private String betterItem;       // "A", "B", "SAME"
}
