package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DamageCalcResponse {

    private int blessingAtk;        // 정령의 축복 공격력 기여
    private int empressBlessingAtk; // 여제의 축복 공격력 기여
    private int buffAtk;            // 전체 버프 공격력 기여
    private int buffMagic;          // 전체 버프 마력 기여
    private double mapleWarriorStat;// 메용 반영 주스탯
    private int critRate;           // 버프 반영 크리티컬 확률
    private int critDmgPP;          // 버프 반영 크리티컬 추가 퍼뎀
    private int totalAtk;           // 총공격력
    private double totalMagic;      // 총마력
    private double damage;          // 스공
}
