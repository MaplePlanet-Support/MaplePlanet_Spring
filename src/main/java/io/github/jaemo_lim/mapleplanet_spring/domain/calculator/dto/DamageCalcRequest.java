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

    @Min(0)
    private int mapleWarriorLevel;    // 메이플 용사 레벨

    @Min(0)
    private int meditationLevel;      // 메디테이션 레벨

    @Min(0)
    private int rageLevel;            // 분노 레벨

    @Min(0)
    private int sharpEyesLevel;       // 샤프아이즈 레벨

    private boolean decentSharpEyes;  // 쓸만한 샤프아이즈

    private boolean babyDragonFood;   // 아기용의 이유식

    private boolean hontaleVictory;   // 혼테일 원정대의 승리 (공+30, 마+40)

    @Min(0)
    private int ammoAtk;              // 화살/표창/불릿 공격력 (공격력%에 영향 안 받음)
}
