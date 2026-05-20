package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto;

public record StatConvResponse(
        double statPct1ToMainStat,
        double allStatPct1ToMainStat,
        double atkPct1ToMainStat,
        double flatAtk1ToMainStat,
        double bossDmgPct1ToMainStat,
        double totalDmgPct1ToMainStat,
        double currentMainStat,
        int finalSubStat,
        int totalAttack,
        double atkPct1,
        double mainStatMultiplier
) {}
