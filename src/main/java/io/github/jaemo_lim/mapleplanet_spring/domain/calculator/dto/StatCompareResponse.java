package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatCompareResponse {

    private double statWithItemA;
    private double statWithItemB;
    private double difference;   // B - A (양수면 B가 유리)
    private String betterItem;   // "A", "B", "SAME"
}
