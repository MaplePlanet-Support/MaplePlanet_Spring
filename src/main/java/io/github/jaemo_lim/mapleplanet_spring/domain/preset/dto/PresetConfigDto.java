package io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PresetConfigDto {

    @Size(max = 30)
    private String job;
    private int mode;
    private int baseStat;
    private int subStat;
    @Size(max = 30)
    private String weaponType;
    @Size(max = 30)
    private String compareWeaponType;
    private int weaponAtk;
    private int atkPercent;
    private int critRate;
    private int critDmgPP;
    private int skillCoeff;
    private int amplificationLevel;
    private int blessingLevel;
    private int empressBlessingLevel;
    private int mapleWarriorLevel;
    private int meditationLevel;
    private int rageLevel;
    private int sharpEyesLevel;
    private boolean decentSharpEyes;
    private boolean babyDragonFood;
    private boolean hontaleVictory;
    private int ammoAtk;
    @Size(max = 30)
    private String ammoName;
    private boolean useOverall;

    @Valid
    @Size(max = 32)
    private Map<String, SlotItemDto> slots;

    @Size(max = 30)
    private String compareSlot;
    @Valid
    private SlotItemDto compareItem;

    // Backward compatibility for old saved JSON.
    @Valid
    private SlotItemDto itemA;
    @Valid
    private SlotItemDto itemB;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SlotItemDto {
        private int flatStat;
        private int flatSubStat;
        private int statPercent;
        private int allStatPercent;
        private int flatAllStat;
        private int subStatPercent;
        private int flatAtk;
        private int atkPercent;
        private int critRateBonus;
        private int totalDmgPercent;
        private int bossDmgPercent;
    }
}
