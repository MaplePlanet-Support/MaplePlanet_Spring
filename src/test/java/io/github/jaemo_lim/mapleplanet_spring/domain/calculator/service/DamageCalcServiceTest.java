package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.service;

import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto.DamageCalcRequest;
import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto.DamageCalcResponse;
import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto.DamageCompareRequest;
import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto.DamageCompareResponse;
import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto.ItemStatFullDto;
import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto.StatConvRequest;
import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto.StatConvResponse;
import io.github.jaemo_lim.mapleplanet_spring.global.enums.WeaponType;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.data.Offset.offset;

class DamageCalcServiceTest {

    private final DamageCalcService service = new DamageCalcService();

    @Test
    void physicalDamageUsesDisplayedIntegerStats() {
        DamageCompareRequest request = new DamageCompareRequest();
        ReflectionTestUtils.setField(request, "baseStat", 101);
        ReflectionTestUtils.setField(request, "subStat", 53);
        ReflectionTestUtils.setField(request, "weaponType", WeaponType.TWO_HAND_SWORD);
        ReflectionTestUtils.setField(request, "weaponAtk", 100);
        ReflectionTestUtils.setField(request, "skillCoeff", 150);
        ReflectionTestUtils.setField(request, "equippedItems", List.of());
        ReflectionTestUtils.setField(request, "itemA", item(0, 0, 10, 10));
        ReflectionTestUtils.setField(request, "itemB", item(0, 0, 10, 10));

        DamageCompareResponse response = service.compare(request);

        assertThat(response.getStatWithItemA()).isCloseTo(111.1, offset(0.000001));
        assertThat(response.getDamageWithItemA()).isEqualTo(568);
    }

    @Test
    void blessingAndEmpressBlessingStack() {
        DamageCalcRequest request = new DamageCalcRequest();
        ReflectionTestUtils.setField(request, "mainStat", 100);
        ReflectionTestUtils.setField(request, "subStat", 50);
        ReflectionTestUtils.setField(request, "weaponType", WeaponType.TWO_HAND_SWORD);
        ReflectionTestUtils.setField(request, "weaponAtk", 100);
        ReflectionTestUtils.setField(request, "blessingLevel", 20);
        ReflectionTestUtils.setField(request, "empressBlessingLevel", 12);

        DamageCalcResponse response = service.calculate(request);

        assertThat(response.getBlessingAtk()).isEqualTo(20);
        assertThat(response.getEmpressBlessingAtk()).isEqualTo(12);
        assertThat(response.getBuffAtk()).isEqualTo(32);
        assertThat(response.getBuffMagic()).isEqualTo(64);
    }

    @Test
    void hontaleVictoryAndMeditationDoNotStackMagicBuffs() {
        DamageCalcRequest request = new DamageCalcRequest();
        ReflectionTestUtils.setField(request, "mainStat", 100);
        ReflectionTestUtils.setField(request, "subStat", 50);
        ReflectionTestUtils.setField(request, "weaponType", WeaponType.STAFF);
        ReflectionTestUtils.setField(request, "weaponAtk", 100);
        ReflectionTestUtils.setField(request, "meditationLevel", 20);
        ReflectionTestUtils.setField(request, "hontaleVictory", true);

        DamageCalcResponse response = service.calculate(request);

        assertThat(response.getBuffMagic()).isEqualTo(40);
    }

    @Test
    void clawWeaponConstantIsThreePointSix() {
        assertThat(WeaponType.CLAW.getConstant()).isEqualTo(3.6);
    }

    @Test
    void archerWeaponConstantsUseMaplePlanetValues() {
        assertThat(WeaponType.BOW.getConstant()).isEqualTo(3.8);
        assertThat(WeaponType.CROSSBOW.getConstant()).isEqualTo(3.6);
    }

    @Test
    void crossbowDamageMatchesDisplayedAttackRangeSample() {
        DamageCompareRequest request = new DamageCompareRequest();
        ReflectionTestUtils.setField(request, "baseStat", 856);
        ReflectionTestUtils.setField(request, "subStat", 105);
        ReflectionTestUtils.setField(request, "weaponType", WeaponType.CROSSBOW);
        ReflectionTestUtils.setField(request, "weaponAtk", 147);
        ReflectionTestUtils.setField(request, "skillCoeff", 150);
        ReflectionTestUtils.setField(request, "equippedItems", List.of());
        ReflectionTestUtils.setField(request, "itemA", item(0, 0, 0, 0));
        ReflectionTestUtils.setField(request, "itemB", item(0, 0, 0, 0));

        DamageCompareResponse response = service.compare(request);

        assertThat(response.getDamageWithItemA()).isEqualTo(4684);
    }

    @Test
    void statConversionTreatsBossAndTotalDamageEquallyForBossDamage() {
        StatConvRequest request = statConvRequest(WeaponType.DAGGER);
        ReflectionTestUtils.setField(request, "equippedItems", List.of(damageItem(6, 60)));

        StatConvResponse response = service.statConv(request);

        assertThat(response.totalDmgPct1ToMainStat()).isEqualTo(response.bossDmgPct1ToMainStat());
    }

    @Test
    void magicStatConversionTreatsBossAndTotalDamageEquallyForBossDamage() {
        StatConvRequest request = statConvRequest(WeaponType.STAFF);
        ReflectionTestUtils.setField(request, "equippedItems", List.of(damageItem(6, 60)));

        StatConvResponse response = service.statConv(request);

        assertThat(response.totalDmgPct1ToMainStat()).isEqualTo(response.bossDmgPct1ToMainStat());
    }

    @Test
    void statConversionIncludesCritRateValueUntilCritCap() {
        StatConvRequest request = statConvRequest(WeaponType.DAGGER);
        ReflectionTestUtils.setField(request, "critRate", 50);
        ReflectionTestUtils.setField(request, "critDmgPP", 100);
        ReflectionTestUtils.setField(request, "skillCoeff", 150);

        StatConvResponse response = service.statConv(request);

        assertThat(response.critRate1ToMainStat()).isGreaterThan(0);

        ReflectionTestUtils.setField(request, "critRate", 100);
        StatConvResponse capped = service.statConv(request);

        assertThat(capped.critRate1ToMainStat()).isZero();
    }

    private static ItemStatFullDto item(int flatStat, int flatSubStat, int statPercent, int subStatPercent) {
        ItemStatFullDto item = new ItemStatFullDto();
        ReflectionTestUtils.setField(item, "flatStat", flatStat);
        ReflectionTestUtils.setField(item, "flatSubStat", flatSubStat);
        ReflectionTestUtils.setField(item, "statPercent", statPercent);
        ReflectionTestUtils.setField(item, "subStatPercent", subStatPercent);
        return item;
    }

    private static StatConvRequest statConvRequest(WeaponType weaponType) {
        StatConvRequest request = new StatConvRequest();
        ReflectionTestUtils.setField(request, "baseStat", 1000);
        ReflectionTestUtils.setField(request, "subStat", 300);
        ReflectionTestUtils.setField(request, "weaponType", weaponType);
        ReflectionTestUtils.setField(request, "weaponAtk", 100);
        return request;
    }

    private static ItemStatFullDto damageItem(int totalDmgPercent, int bossDmgPercent) {
        ItemStatFullDto item = new ItemStatFullDto();
        ReflectionTestUtils.setField(item, "totalDmgPercent", totalDmgPercent);
        ReflectionTestUtils.setField(item, "bossDmgPercent", bossDmgPercent);
        return item;
    }
}
