package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto;

import io.github.jaemo_lim.mapleplanet_spring.global.enums.WeaponType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class DamageCompareRequest {

    @Min(0)
    private int baseStat;

    @Min(0)
    private int subStat;

    @NotNull
    private WeaponType weaponType;

    private WeaponType itemBWeaponType;

    @Min(0)
    private int weaponAtk;

    @Min(0)
    private int atkPercent;

    @Min(0)
    private int blessingLevel;

    @Min(0)
    private int empressBlessingLevel;

    @Min(0)
    private int mapleWarriorLevel;

    @Min(0)
    private int meditationLevel;

    @Min(0)
    private int rageLevel;

    @Min(0)
    private int sharpEyesLevel;

    private boolean decentSharpEyes;

    private boolean babyDragonFood;

    @Min(0)
    private int critRate;

    @Min(0)
    private int critDmgPP; // 크리 시 추가 퍼뎀 절대값(%p). 예) SE30=140, CT30=100

    @Min(1)
    private int skillCoeff = 150; // 기준 스킬 계수(%p). 예) 럭키세븐=150, 파워스트라이크=260

    @Valid
    @Size(max = 32)
    private List<ItemStatFullDto> equippedItems;

    @Valid
    private ItemStatFullDto currentSlotItem;

    @NotNull
    @Valid
    private ItemStatFullDto itemA;

    @NotNull
    @Valid
    private ItemStatFullDto itemB;
}
