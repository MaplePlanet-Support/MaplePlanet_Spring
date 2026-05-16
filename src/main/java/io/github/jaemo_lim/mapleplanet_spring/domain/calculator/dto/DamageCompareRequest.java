package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto;

import io.github.jaemo_lim.mapleplanet_spring.global.enums.WeaponType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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

    @Min(0)
    private int weaponAtk;

    @Min(0)
    private int atkPercent;

    @Min(0)
    private int blessingLevel;

    @Min(0)
    private int empressBlessingLevel;

    @Valid
    private List<ItemStatFullDto> equippedItems;

    @Valid
    private ItemStatFullDto currentSlotItem; // null = 빈 슬롯

    @NotNull @Valid
    private ItemStatFullDto itemA;

    @NotNull @Valid
    private ItemStatFullDto itemB;
}
