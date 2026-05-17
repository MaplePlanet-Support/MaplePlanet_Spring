package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.service;

import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DamageCalcService {

    private static final int SPIRIT_BLESSING_MAX_LEVEL = 20;
    private static final int EMPRESS_BLESSING_MAX_LEVEL = 12;
    private static final int MAPLE_WARRIOR_MAX_LEVEL = 30;
    private static final int MEDITATION_MAX_LEVEL = 20;
    private static final int RAGE_MAX_LEVEL = 20;
    private static final int SHARP_EYES_MAX_LEVEL = 30;

    public DamageCalcResponse calculate(DamageCalcRequest req) {
        BuffBonus buff = BuffBonus.from(req);
        boolean magicWeapon = req.getWeaponType().isMagicWeapon();
        double buffedMainStat = applyMapleWarrior(req.getMainStat(), buff.mapleWarriorPercent());
        int totalAtk = 0;
        int totalMagic = 0;
        double damage;

        if (magicWeapon) {
            totalMagic = calcTotalAttackValue(
                    req.getWeaponAtk() + req.getEquipAtk() + buff.magic(),
                    req.getAtkPercent()
            );
            damage = calcMagicDamage(buffedMainStat, totalMagic);
        } else {
            totalAtk = calcTotalAttackValue(
                    req.getWeaponAtk() + req.getEquipAtk() + buff.attack(),
                    req.getAtkPercent()
            );
            damage = calcPhysicalDamage(
                    buffedMainStat,
                    req.getSubStat(),
                    req.getWeaponType().getConstant(),
                    totalAtk
            );
        }

        return new DamageCalcResponse(
                buff.spiritAttack(),
                buff.empressAttack(),
                buff.attack(),
                buff.magic(),
                buffedMainStat,
                buff.critRate(),
                buff.critDmgPP(),
                totalAtk,
                totalMagic,
                damage
        );
    }

    public DamageCompareResponse compare(DamageCompareRequest req) {
        BuffBonus buff = BuffBonus.from(req);

        int totalFlatStat = sum(req.getEquippedItems(), "flat");
        int totalFlatSubStat = sum(req.getEquippedItems(), "subFlat");
        int totalStatPercent = sum(req.getEquippedItems(), "percent");
        int totalFlatAtk = sum(req.getEquippedItems(), "atk");
        int totalAtkPercent = sum(req.getEquippedItems(), "atkPercent");
        int totalCritRate = sum(req.getEquippedItems(), "critRate");
        int totalDmgPercent = sum(req.getEquippedItems(), "totalDmg");
        int totalBossDmgPercent = sum(req.getEquippedItems(), "bossDmg");

        if (req.getCurrentSlotItem() != null) {
            totalFlatStat -= req.getCurrentSlotItem().getFlatStat();
            totalFlatSubStat -= req.getCurrentSlotItem().getFlatSubStat();
            totalStatPercent -= req.getCurrentSlotItem().getStatPercent();
            totalFlatAtk -= req.getCurrentSlotItem().getFlatAtk();
            totalAtkPercent -= req.getCurrentSlotItem().getAtkPercent();
            totalCritRate -= req.getCurrentSlotItem().getCritRateBonus();
            totalDmgPercent -= req.getCurrentSlotItem().getTotalDmgPercent();
            totalBossDmgPercent -= req.getCurrentSlotItem().getBossDmgPercent();
        }

        double statA = calcStat(req.getBaseStat(), buff.mapleWarriorPercent(), totalFlatStat, totalStatPercent, req.getItemA());
        double statB = calcStat(req.getBaseStat(), buff.mapleWarriorPercent(), totalFlatStat, totalStatPercent, req.getItemB());

        DamageResult resultA = calcDamage(req, buff, statA, totalFlatSubStat, totalFlatAtk, totalAtkPercent,
                totalCritRate, totalDmgPercent, totalBossDmgPercent, req.getItemA());
        DamageResult resultB = calcDamage(req, buff, statB, totalFlatSubStat, totalFlatAtk, totalAtkPercent,
                totalCritRate, totalDmgPercent, totalBossDmgPercent, req.getItemB());

        double expectedDiffPct = resultA.expectedDamage() == 0
                ? 0
                : (resultB.expectedDamage() - resultA.expectedDamage()) / resultA.expectedDamage() * 100.0;
        String better = expectedDiffPct > 0 ? "B" : expectedDiffPct < 0 ? "A" : "SAME";

        return new DamageCompareResponse(
                statA,
                statB,
                resultA.damage(),
                resultB.damage(),
                resultA.magic(),
                resultB.magic(),
                resultA.expectedDamage(),
                resultB.expectedDamage(),
                statB - statA,
                resultB.damage() - resultA.damage(),
                expectedDiffPct,
                better
        );
    }

    private double calcStat(int baseStat, int mapleWarriorPercent, int totalFlat, int totalPercent, ItemStatFullDto item) {
        double directStat = applyMapleWarrior(baseStat, mapleWarriorPercent);
        return (directStat + totalFlat + item.getFlatStat())
                * (1 + (totalPercent + item.getStatPercent()) / 100.0);
    }

    private DamageResult calcDamage(DamageCompareRequest req, BuffBonus buff, double mainStat,
                                    int totalFlatSubStat, int totalFlatAtk, int totalAtkPercent, int totalCritRate,
                                    int totalDmgPercent, int totalBossDmgPercent, ItemStatFullDto item) {
        boolean magicWeapon = req.getWeaponType().isMagicWeapon();
        int attackPercent = req.getAtkPercent() + totalAtkPercent + item.getAtkPercent();
        int subStat = req.getSubStat() + totalFlatSubStat + item.getFlatSubStat();
        double damage;
        double magic = 0;

        if (magicWeapon) {
            magic = calcTotalAttackValue(
                    (int) Math.floor(req.getWeaponAtk() + totalFlatAtk + item.getFlatAtk() + buff.magic()),
                    attackPercent
            );
            damage = calcMagicDamage(mainStat, magic);
        } else {
            int totalAtk = calcTotalAttackValue(
                    req.getWeaponAtk() + totalFlatAtk + item.getFlatAtk() + buff.attack(),
                    attackPercent
            );
            damage = calcPhysicalDamage(mainStat, subStat, req.getWeaponType().getConstant(), totalAtk);
        }

        int critRate = clamp(req.getCritRate() + totalCritRate + item.getCritRateBonus() + buff.critRate(), 0, 100);
        int critDmgPP = req.getCritDmgPP() + buff.critDmgPP(); // 절대값(%p)
        int skillCoeff = req.getSkillCoeff() > 0 ? req.getSkillCoeff() : 150;
        int damagePercent = totalDmgPercent + totalBossDmgPercent + item.getTotalDmgPercent() + item.getBossDmgPercent();
        double skillMultiplier = 1.0;
        double damageMultiplier = 1 + damagePercent / 100.0;
        // 올바른 공식: 1 + 크리확 × 크리보너스절대값 / 스킬계수
        // 예) SE30(140%p) + 파워스트라이크(260%p): 크리배율 = (260+140)/260 = 1.538
        double critMultiplier = critRate > 0
                ? 1 + (critRate / 100.0) * (critDmgPP / (double) skillCoeff)
                : 1.0;

        return new DamageResult(damage, magic, damage * skillMultiplier * damageMultiplier * critMultiplier);
    }

    private int calcTotalAttackValue(int baseAttack, int attackPercent) {
        return (int) Math.floor(baseAttack * (1 + attackPercent / 100.0));
    }

    private double calcPhysicalDamage(double mainStat, int subStat, double weaponConst, int totalAtk) {
        return (mainStat * weaponConst + subStat) * totalAtk / 100.0;
    }

    private double calcMagicDamage(double mainStat, double totalMagic) {
        return 0.0033665 * totalMagic * totalMagic + 3.3 * totalMagic + 0.5 * mainStat;
    }

    private double applyMapleWarrior(int baseStat, int mapleWarriorPercent) {
        return baseStat + Math.floor(baseStat * mapleWarriorPercent / 100.0);
    }

    private int sum(List<ItemStatFullDto> items, String type) {
        if (items == null) return 0;
        return switch (type) {
            case "flat" -> items.stream().mapToInt(ItemStatFullDto::getFlatStat).sum();
            case "subFlat" -> items.stream().mapToInt(ItemStatFullDto::getFlatSubStat).sum();
            case "percent" -> items.stream().mapToInt(ItemStatFullDto::getStatPercent).sum();
            case "atk" -> items.stream().mapToInt(ItemStatFullDto::getFlatAtk).sum();
            case "atkPercent" -> items.stream().mapToInt(ItemStatFullDto::getAtkPercent).sum();
            case "critRate" -> items.stream().mapToInt(ItemStatFullDto::getCritRateBonus).sum();
            case "totalDmg" -> items.stream().mapToInt(ItemStatFullDto::getTotalDmgPercent).sum();
            case "bossDmg" -> items.stream().mapToInt(ItemStatFullDto::getBossDmgPercent).sum();
            default -> 0;
        };
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private record DamageResult(double damage, double magic, double expectedDamage) {
    }

    private record BuffBonus(
            int spiritAttack,
            int empressAttack,
            int attack,
            int magic,
            int mapleWarriorPercent,
            int critRate,
            int critDmgPP
    ) {
        static BuffBonus from(DamageCalcRequest req) {
            return from(
                    req.getBlessingLevel(),
                    req.getEmpressBlessingLevel(),
                    req.getMapleWarriorLevel(),
                    req.getMeditationLevel(),
                    req.getRageLevel(),
                    req.getSharpEyesLevel(),
                    req.isDecentSharpEyes(),
                    req.isBabyDragonFood()
            );
        }

        static BuffBonus from(DamageCompareRequest req) {
            return from(
                    req.getBlessingLevel(),
                    req.getEmpressBlessingLevel(),
                    req.getMapleWarriorLevel(),
                    req.getMeditationLevel(),
                    req.getRageLevel(),
                    req.getSharpEyesLevel(),
                    req.isDecentSharpEyes(),
                    req.isBabyDragonFood()
            );
        }

        private static BuffBonus from(int spiritLevel, int empressLevel, int mapleWarriorLevel,
                                      int meditationLevel, int rageLevel, int sharpEyesLevel,
                                      boolean decentSharpEyes, boolean babyDragonFood) {
            int spirit = clamp(spiritLevel, 0, SPIRIT_BLESSING_MAX_LEVEL);
            int empress = clamp(empressLevel, 0, EMPRESS_BLESSING_MAX_LEVEL);
            int sharpEyes = clamp(sharpEyesLevel, 0, SHARP_EYES_MAX_LEVEL);
            int spiritAttack = spirit;
            int empressAttack = empress;
            int attack = spiritAttack
                    + empressAttack
                    + rageAttack(clamp(rageLevel, 0, RAGE_MAX_LEVEL))
                    + (babyDragonFood ? 7 : 0);
            int magic = spirit * 2
                    + empress * 2
                    + clamp(meditationLevel, 0, MEDITATION_MAX_LEVEL);
            // 샾30 = +140%p 절대값 (합연산), 선형 보간
            int critRate = sharpEyes == 0 ? 0 : (int) Math.floor((sharpEyes + 1) / 2.0);
            int critDmgPP = sharpEyes == 0 ? 0 : (int) Math.round(sharpEyes * 140.0 / 30.0);

            if (decentSharpEyes) {
                critRate += 10;
                critDmgPP += 115; // 쓸샾 = +115%p 절대값 (합연산)
            }

            return new BuffBonus(
                    spiritAttack,
                    empressAttack,
                    attack,
                    magic,
                    mapleWarriorPercent(clamp(mapleWarriorLevel, 0, MAPLE_WARRIOR_MAX_LEVEL)),
                    critRate,
                    critDmgPP
            );
        }

        private static int mapleWarriorPercent(int level) {
            return level == 0 ? 0 : (int) Math.floor((level + 1) / 2.0);
        }

        private static int rageAttack(int level) {
            if (level == 0) return 0;
            if (level <= 16) return (int) Math.floor((level + 1) / 2.0);
            return 8 + (level - 16);
        }
    }
}
