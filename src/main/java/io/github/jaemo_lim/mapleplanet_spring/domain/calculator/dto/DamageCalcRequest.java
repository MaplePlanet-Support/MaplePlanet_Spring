package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto;

import io.github.jaemo_lim.mapleplanet_spring.global.enums.WeaponType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DamageCalcRequest {

    @Min(0)
    private int mainStat;           // 주스탯

    @Min(0)
    private int subStat;            // 부스탯

    @NotNull
    private WeaponType weaponType;  // 무기 종류 (상수 자동 적용)

    @Min(0)
    private int weaponAtk;          // 무기 깡공

    @Min(0)
    private int equipAtk;           // 장비 공격력 합 (무기 제외)

    @Min(0)
    private int atkPercent;         // 공격력%

    @Min(0)
    private int blessingLevel;      // 정령의 축복 레벨 (0 = 미적용)

    @Min(0)
    private int empressBlessingLevel; // 여제의 축복 레벨 (0 = 미적용)
}
