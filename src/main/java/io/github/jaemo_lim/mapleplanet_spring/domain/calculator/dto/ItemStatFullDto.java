package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemStatFullDto {

    @Min(0)
    private int flatStat;    // 깡 주스탯

    @Min(0)
    private int statPercent; // 주스탯%

    @Min(0)
    private int flatAtk;     // 깡 공격력/마력
}
