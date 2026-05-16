package io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PresetConfigDto {

    private String job;
    private int mode;
    private int baseStat;
    private int subStat;
    private String weaponType;
    private int weaponAtk;
    private int atkPercent;
    private int blessingLevel;
    private int empressBlessingLevel;
    private boolean useOverall;

    private Map<String, SlotItemDto> slots;

    private String compareSlot;
    private SlotItemDto itemA;
    private SlotItemDto itemB;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SlotItemDto {
        private int flatStat;
        private int statPercent;
        private int flatAtk;
    }
}
