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
    private int weaponAtk;
    private int atkPercent;
    private int blessingLevel;
    private int empressBlessingLevel;
    private boolean useOverall;

    @Valid
    @Size(max = 32)
    private Map<String, SlotItemDto> slots;

    @Size(max = 30)
    private String compareSlot;
    @Valid
    private SlotItemDto itemA;
    @Valid
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
