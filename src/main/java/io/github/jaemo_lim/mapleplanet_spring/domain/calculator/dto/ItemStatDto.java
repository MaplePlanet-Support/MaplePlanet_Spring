package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemStatDto {

    @Min(0)
    private int flatStat;

    @Min(0)
    private int statPercent;
}
