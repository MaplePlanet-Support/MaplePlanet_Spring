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
    private int flatSubStat; // 깡 부스탯

    @Min(0)
    private int statPercent; // 주스탯%

    @Min(0)
    private int flatAtk;     // 깡 공격력/마력

    @Min(0)
    private int atkPercent;  // 공격력/마력%

    @Min(0)
    private int critRateBonus; // 크리티컬 확률%

    @Min(0)
    private int totalDmgPercent; // 총 데미지%

    @Min(0)
    private int bossDmgPercent; // 보스 공격 시 데미지%

    @Min(0)
    private int allStatPercent; // 올스탯%

    @Min(0)
    private int flatAllStat;    // 깡 올스탯

    @Min(0)
    private int subStatPercent; // 부스탯%
}
