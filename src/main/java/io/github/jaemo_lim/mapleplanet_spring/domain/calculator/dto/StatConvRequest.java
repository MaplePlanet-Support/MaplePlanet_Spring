package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import io.github.jaemo_lim.mapleplanet_spring.global.enums.WeaponType;

import java.util.List;

@Getter
@NoArgsConstructor
public class StatConvRequest {

    @Min(0)
    private int baseStat;

    @Min(0)
    private int subStat;

    @NotNull
    private WeaponType weaponType;

    @Min(0)
    private int weaponAtk;

    @Min(0)
    private int mapleWarriorLevel;

    @Min(0)
    private int blessingLevel;

    @Min(0)
    private int empressBlessingLevel;

    @Min(0)
    private int meditationLevel;

    @Min(0)
    private int rageLevel;

    private boolean babyDragonFood;

    private boolean hontaleVictory;

    @Min(0)
    private int ammoAtk;

    @Valid
    @Size(max = 32)
    private List<ItemStatFullDto> equippedItems;
}
