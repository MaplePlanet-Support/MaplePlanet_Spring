package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DamageCalcResponse {

    private int blessingAtk;        // 정령의 축복 공격력 기여
    private int empressBlessingAtk; // 여제의 축복 공격력 기여
    private int totalAtk;           // 총공격력
    private double damage;          // 스공
}
