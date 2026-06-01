package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.service;

import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto.*;
import io.github.jaemo_lim.mapleplanet_spring.global.enums.WeaponType;
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
    private static final int DEFAULT_SKILL_COEFF = 150;
    private static final int DEFAULT_MAGIC_SKILL_COEFF = 100;

    public DamageCalcResponse calculate(DamageCalcRequest req) {
        BuffBonus buff = BuffBonus.from(req);
        boolean magicWeapon = req.getWeaponType().isMagicWeapon();
        double buffedMainStat = applyMapleWarrior(req.getMainStat(), buff.mapleWarriorPercent());
        int totalAtk = 0;
        double totalMagic = 0;
        double damage;

        if (magicWeapon) {

            totalMagic = calcTotalMagicValue(
                    (int) Math.floor(buffedMainStat) + req.getWeaponAtk() + req.getEquipAtk(),
                    req.getAtkPercent()
            ) + buff.magic();
            damage = calcMagicDamage(totalMagic, buffedMainStat, DEFAULT_MAGIC_SKILL_COEFF);
        } else {
            totalAtk = calcPhysicalTotalAttack(req.getWeaponAtk(), req.getEquipAtk(), buff.attack(), req.getAmmoAtk(), req.getAtkPercent());
            double buffedSubStat = applyMapleWarrior(req.getSubStat(), buff.mapleWarriorPercent());
            damage = calcPhysicalDamage(
                    buffedMainStat,
                    buffedSubStat,
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
        int totalAllStatPercent = sum(req.getEquippedItems(), "allStatPercent");
        int totalFlatAllStat = sum(req.getEquippedItems(), "flatAllStat");
        int totalSubStatPercent = sum(req.getEquippedItems(), "subStatPercent");
        int totalFlatAtk = sum(req.getEquippedItems(), "atk");
        int totalAtkPercent = sum(req.getEquippedItems(), "atkPercent");
        int totalCritRate = sum(req.getEquippedItems(), "critRate");
        int totalDmgPercent = sum(req.getEquippedItems(), "totalDmg");
        int totalBossDmgPercent = sum(req.getEquippedItems(), "bossDmg");

        if (req.getCurrentSlotItem() != null) {
            totalFlatStat -= req.getCurrentSlotItem().getFlatStat();
            totalFlatSubStat -= req.getCurrentSlotItem().getFlatSubStat();
            totalStatPercent -= req.getCurrentSlotItem().getStatPercent();
            totalAllStatPercent -= req.getCurrentSlotItem().getAllStatPercent();
            totalFlatAllStat -= req.getCurrentSlotItem().getFlatAllStat();
            totalSubStatPercent -= req.getCurrentSlotItem().getSubStatPercent();
            totalFlatAtk -= req.getCurrentSlotItem().getFlatAtk();
            totalAtkPercent -= req.getCurrentSlotItem().getAtkPercent();
            totalCritRate -= req.getCurrentSlotItem().getCritRateBonus();
            totalDmgPercent -= req.getCurrentSlotItem().getTotalDmgPercent();
            totalBossDmgPercent -= req.getCurrentSlotItem().getBossDmgPercent();
        }

        double statA = calcStat(req.getBaseStat(), buff.mapleWarriorPercent(), totalFlatStat, totalFlatAllStat, totalStatPercent, totalAllStatPercent, req.getItemA());
        double statB = calcStat(req.getBaseStat(), buff.mapleWarriorPercent(), totalFlatStat, totalFlatAllStat, totalStatPercent, totalAllStatPercent, req.getItemB());

        DamageResult resultA = calcDamage(req, buff, req.getWeaponType(), statA, totalFlatSubStat, totalFlatAllStat, totalSubStatPercent, totalFlatAtk, totalAtkPercent,
                totalCritRate, totalDmgPercent, totalBossDmgPercent, totalAllStatPercent, req.getItemA());
        DamageResult resultB = calcDamage(req, buff, getItemBWeaponType(req), statB, totalFlatSubStat, totalFlatAllStat, totalSubStatPercent, totalFlatAtk, totalAtkPercent,
                totalCritRate, totalDmgPercent, totalBossDmgPercent, totalAllStatPercent, req.getItemB());

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
                resultA.normalExpectedDamage(),
                resultB.normalExpectedDamage(),
                statB - statA,
                resultB.damage() - resultA.damage(),
                expectedDiffPct,
                better
        );
    }

    private double calcStat(int baseStat, int mapleWarriorPercent, int totalFlat, int totalFlatAllStat, int totalPercent, int totalAllStatPercent, ItemStatFullDto item) {
        double mapleWarriorBonus = Math.floor(baseStat * mapleWarriorPercent / 100.0);
        int effectivePercent = totalPercent + totalAllStatPercent + item.getStatPercent() + item.getAllStatPercent();
        double baseAndFlat = baseStat + totalFlat + totalFlatAllStat + item.getFlatStat() + item.getFlatAllStat();
        return baseAndFlat * (1 + effectivePercent / 100.0) + mapleWarriorBonus;
    }

    private DamageResult calcDamage(DamageCompareRequest req, BuffBonus buff, WeaponType weaponType, double mainStat,
                                    int totalFlatSubStat, int totalFlatAllStat, int totalSubStatPercent, int totalFlatAtk, int totalAtkPercent, int totalCritRate,
                                    int totalDmgPercent, int totalBossDmgPercent, int totalAllStatPercent, ItemStatFullDto item) {
        boolean magicWeapon = weaponType.isMagicWeapon();
        int attackPercent = totalAtkPercent + item.getAtkPercent();
        double mapleWarriorSubBonus = Math.floor(req.getSubStat() * buff.mapleWarriorPercent() / 100.0);
        int flatSubTotal = req.getSubStat() + totalFlatSubStat + totalFlatAllStat + item.getFlatSubStat() + item.getFlatAllStat();
        int effectiveSubPercent = totalAllStatPercent + totalSubStatPercent + item.getAllStatPercent() + item.getSubStatPercent();
        double subStat = flatSubTotal * (1 + effectiveSubPercent / 100.0) + mapleWarriorSubBonus;
        int skillCoeff = req.getSkillCoeff() > 0
                ? req.getSkillCoeff()
                : (magicWeapon ? DEFAULT_MAGIC_SKILL_COEFF : DEFAULT_SKILL_COEFF);
        double damage;
        double magic = 0;

        if (magicWeapon) {
            // 총마력 = (주스탯 + 무기마력 + 장비마력 + 비교아이템마력) * (1 + 마력%) + 버프마력
            magic = calcTotalMagicValue(
                    (int) Math.floor(mainStat) + req.getWeaponAtk() + totalFlatAtk + item.getFlatAtk(),
                    attackPercent
            ) + buff.magic();
            damage = calcMagicDamage(magic, mainStat, skillCoeff);
        } else {
            boolean comparingWeapon = req.getWeaponAtk() == 0 && item.getFlatAtk() > 0;
            int weaponAtk = comparingWeapon ? item.getFlatAtk() : req.getWeaponAtk();
            int equipAtk = comparingWeapon ? totalFlatAtk : totalFlatAtk + item.getFlatAtk();
            int totalAtk = calcPhysicalTotalAttack(weaponAtk, equipAtk, buff.attack(), req.getAmmoAtk(), attackPercent);
            damage = calcPhysicalDamage(mainStat, subStat, weaponType.getConstant(), totalAtk);
        }

        int critRate = clamp(req.getCritRate() + totalCritRate + item.getCritRateBonus() + buff.critRate(), 0, 100);
        int critDmgPP = req.getCritDmgPP() + buff.critDmgPP(); // ?덈?媛?%p)
        int normalDmgPercent = totalDmgPercent + item.getTotalDmgPercent();
        int bossDmgPercent   = normalDmgPercent + totalBossDmgPercent + item.getBossDmgPercent();
        // ?щ컮瑜?怨듭떇: 1 + ?щ━??횞 ?щ━蹂대꼫?ㅼ젅?媛?/ ?ㅽ궗怨꾩닔
        // ?? SE30(140%p) + ?뚯썙?ㅽ듃?쇱씠??260%p): ?щ━諛곗쑉 = (260+140)/260 = 1.538
        double critMultiplier = critRate > 0
                ? 1 + (critRate / 100.0) * (critDmgPP / (double) skillCoeff)
                : 1.0;
        double normalExpected = damage * (1 + normalDmgPercent / 100.0) * critMultiplier;
        double bossExpected   = damage * (1 + bossDmgPercent   / 100.0) * critMultiplier;

        return new DamageResult(damage, magic, bossExpected, normalExpected);
    }

    public StatConvResponse statConv(StatConvRequest req) {
        BuffBonus buff = BuffBonus.from(req);
        double buffedBaseStat = applyMapleWarrior(req.getBaseStat(), buff.mapleWarriorPercent());
        boolean magicWeapon = req.getWeaponType().isMagicWeapon();

        int totalFlatStat = sum(req.getEquippedItems(), "flat");
        int totalFlatAllStat = sum(req.getEquippedItems(), "flatAllStat");
        int totalFlatSubStat = sum(req.getEquippedItems(), "subFlat");
        int totalStatPct = sum(req.getEquippedItems(), "percent");
        int totalSubStatPct = sum(req.getEquippedItems(), "subStatPercent");
        int totalAllStatPct = sum(req.getEquippedItems(), "allStatPercent");
        int totalAtkPercent = sum(req.getEquippedItems(), "atkPercent");
        int totalEquipAtk = sum(req.getEquippedItems(), "atk");
        int totalBossDmgPct = sum(req.getEquippedItems(), "bossDmg");
        int totalDmgPct = sum(req.getEquippedItems(), "totalDmg");

        double mapleWarriorBonus = Math.floor(req.getBaseStat() * buff.mapleWarriorPercent() / 100.0);
        double mapleWarriorSubBonus = Math.floor(req.getSubStat() * buff.mapleWarriorPercent() / 100.0);
        double flatMainSum = req.getBaseStat() + totalFlatStat + totalFlatAllStat;
        int flatSubSum = req.getSubStat() + totalFlatSubStat + totalFlatAllStat;
        int totalMainPct = totalStatPct + totalAllStatPct;
        int totalSubPct = totalSubStatPct + totalAllStatPct;
        double mainStatMultiplier = 1 + totalMainPct / 100.0;

        double currentMainStat = flatMainSum * mainStatMultiplier + mapleWarriorBonus;
        int finalSubStat = (int) Math.floor(flatSubSum * (1 + totalSubPct / 100.0) + mapleWarriorSubBonus);
        double statPct1ToMainStat = flatMainSum / 100.0;
        double allStatPct1ToMainStat = magicWeapon
                ? flatMainSum / 100.0
                : flatMainSum / 100.0 + flatSubSum / 100.0 / req.getWeaponType().getConstant();

        int totalAttack;
        double atkPct1;
        double flatAtk1ToMainStat;
        double atkPct1ToMainStat;
        double bossDmgPct1ToMainStat;
        double totalDmgPct1ToMainStat;

        if (magicWeapon) {
            MagicStatContext magicContext = new MagicStatContext(
                    flatMainSum,
                    mainStatMultiplier,
                    mapleWarriorBonus,
                    req.getWeaponAtk(),
                    totalEquipAtk,
                    totalAtkPercent,
                    buff.magic(),
                    DEFAULT_MAGIC_SKILL_COEFF
            );
            int magicBaseWithoutBuff = calcMagicBaseWithoutBuff(magicContext, 0);
            totalAttack = (int) Math.floor(calcTotalMagicValue(magicBaseWithoutBuff, totalAtkPercent) + buff.magic());
            atkPct1 = magicBaseWithoutBuff / 100.0;

            double statPctTargetDamage = calcMagicDamage(magicContext.withMainStatMultiplier(mainStatMultiplier + 0.01), 0);
            statPct1ToMainStat = findMagicFinalMainStatEquivalent(magicContext, statPctTargetDamage, 0);
            allStatPct1ToMainStat = statPct1ToMainStat;
            flatAtk1ToMainStat = 1;
            atkPct1ToMainStat = atkPct1 * flatAtk1ToMainStat;

            int bossDamagePercent = totalDmgPct + totalBossDmgPct;
            double bossDmgTarget = calcMagicExpectedDamage(magicContext, 0, bossDamagePercent + 1);
            double totalDmgTarget = calcMagicExpectedDamage(magicContext, 0, bossDamagePercent + 1);
            bossDmgPct1ToMainStat = findMagicFinalMainStatEquivalent(magicContext, bossDmgTarget, bossDamagePercent);
            totalDmgPct1ToMainStat = findMagicFinalMainStatEquivalent(magicContext, totalDmgTarget, bossDamagePercent);
        } else {
            totalAttack = calcPhysicalTotalAttack(req.getWeaponAtk(), totalEquipAtk, buff.attack(), req.getAmmoAtk(), totalAtkPercent);
            atkPct1 = req.getWeaponAtk() / 100.0;
            double damageStat = currentMainStat + finalSubStat / req.getWeaponType().getConstant();
            flatAtk1ToMainStat = totalAttack == 0 ? 0 : damageStat / totalAttack;
            atkPct1ToMainStat = atkPct1 * flatAtk1ToMainStat;
            double bossDamagePercent = totalDmgPct + totalBossDmgPct;
            bossDmgPct1ToMainStat = damageStat / (100.0 + bossDamagePercent);
            totalDmgPct1ToMainStat = damageStat / (100.0 + bossDamagePercent);
        }

        return new StatConvResponse(
                round2(statPct1ToMainStat / mainStatMultiplier),
                round2(allStatPct1ToMainStat / mainStatMultiplier),
                round2(atkPct1ToMainStat / mainStatMultiplier),
                round2(flatAtk1ToMainStat / mainStatMultiplier),
                round2(bossDmgPct1ToMainStat / mainStatMultiplier),
                round2(totalDmgPct1ToMainStat / mainStatMultiplier),
                round2(currentMainStat),
                finalSubStat,
                totalAttack,
                round2(atkPct1),
                round2(mainStatMultiplier)
        );
    }
    private WeaponType getItemBWeaponType(DamageCompareRequest req) {
        return req.getItemBWeaponType() != null ? req.getItemBWeaponType() : req.getWeaponType();
    }

    private int calcTotalAttackValue(int baseAttack, int attackPercent) {
        return (int) Math.floor(baseAttack * (1 + attackPercent / 100.0));
    }

    private double calcTotalMagicValue(int baseMagic, int magicPercent) {
        return baseMagic * (1 + magicPercent / 100.0);
    }

    private int calcPhysicalTotalAttack(int weaponAtk, int equipAtk, int buffAtk, int ammoAtk, int attackPercent) {
        return calcTotalAttackValue(weaponAtk, attackPercent) + equipAtk + buffAtk + ammoAtk;
    }

    private double calcPhysicalDamage(double mainStat, double subStat, double weaponConst, int totalAtk) {
        int finalMainStat = (int) Math.floor(mainStat);
        int finalSubStat = (int) Math.floor(subStat);
        return Math.floor((finalMainStat * weaponConst + finalSubStat) * totalAtk / 100.0);
    }

    private double calcMagicDamage(double totalMagic, double intStat, int skillCoeff) {
        // 珥앸쭏?μ뿉 INT媛 ?ы븿???댄썑 怨듭떇 - ?꾩슂???섏젙
        int finalInt = (int) Math.floor(intStat);
        double baseDamage = ((totalMagic * totalMagic / 1000.0 + totalMagic) / 30.0)
                + finalInt / 200.0;
        return baseDamage * skillCoeff;
    }

    private int calcMagicBaseWithoutBuff(MagicStatContext context, double rawFlatStatBonus) {
        double mainStat = calcMagicMainStat(context, rawFlatStatBonus);
        return (int) Math.floor(mainStat) + context.weaponMagic() + context.equipMagic();
    }

    private double calcMagicMainStat(MagicStatContext context, double rawFlatStatBonus) {
        return (context.flatMainSum() + rawFlatStatBonus) * context.mainStatMultiplier()
                + context.mapleWarriorBonus();
    }

    private double calcMagicDamage(MagicStatContext context, double rawFlatStatBonus) {
        double mainStat = calcMagicMainStat(context, rawFlatStatBonus);
        int magicBaseWithoutBuff = (int) Math.floor(mainStat) + context.weaponMagic() + context.equipMagic();
        double totalMagic = calcTotalMagicValue(magicBaseWithoutBuff, context.magicPercent()) + context.buffMagic();
        return calcMagicDamage(totalMagic, mainStat, context.skillCoeff());
    }

    private double calcMagicExpectedDamage(MagicStatContext context, double rawFlatStatBonus, int damagePercent) {
        return calcMagicDamage(context, rawFlatStatBonus) * (1 + damagePercent / 100.0);
    }

    private double findMagicFinalMainStatEquivalent(MagicStatContext context, double targetExpectedDamage, int damagePercent) {
        double currentExpectedDamage = calcMagicExpectedDamage(context, 0, damagePercent);
        if (targetExpectedDamage <= currentExpectedDamage) {
            return 0;
        }

        double low = 0;
        double high = 1;
        while (calcMagicExpectedDamage(context, high, damagePercent) < targetExpectedDamage && high < 1_000_000) {
            high *= 2;
        }

        for (int i = 0; i < 60; i++) {
            double mid = (low + high) / 2.0;
            if (calcMagicExpectedDamage(context, mid, damagePercent) < targetExpectedDamage) {
                low = mid;
            } else {
                high = mid;
            }
        }

        return high * context.mainStatMultiplier();
    }

    private double applyMapleWarrior(int baseStat, int mapleWarriorPercent) {
        return baseStat + Math.floor(baseStat * mapleWarriorPercent / 100.0);
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private int sum(List<ItemStatFullDto> items, String type) {
        if (items == null) return 0;
        return switch (type) {
            case "flat" -> items.stream().mapToInt(ItemStatFullDto::getFlatStat).sum();
            case "subFlat" -> items.stream().mapToInt(ItemStatFullDto::getFlatSubStat).sum();
            case "percent" -> items.stream().mapToInt(ItemStatFullDto::getStatPercent).sum();
            case "allStatPercent" -> items.stream().mapToInt(ItemStatFullDto::getAllStatPercent).sum();
            case "flatAllStat" -> items.stream().mapToInt(ItemStatFullDto::getFlatAllStat).sum();
            case "subStatPercent" -> items.stream().mapToInt(ItemStatFullDto::getSubStatPercent).sum();
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

    private record DamageResult(double damage, double magic, double expectedDamage, double normalExpectedDamage) {
    }

    private record MagicStatContext(
            double flatMainSum,
            double mainStatMultiplier,
            double mapleWarriorBonus,
            int weaponMagic,
            int equipMagic,
            int magicPercent,
            int buffMagic,
            int skillCoeff
    ) {
        private MagicStatContext withMainStatMultiplier(double value) {
            return new MagicStatContext(flatMainSum, value, mapleWarriorBonus, weaponMagic, equipMagic, magicPercent, buffMagic, skillCoeff);
        }

        private MagicStatContext withEquipMagic(int value) {
            return new MagicStatContext(flatMainSum, mainStatMultiplier, mapleWarriorBonus, weaponMagic, value, magicPercent, buffMagic, skillCoeff);
        }

        private MagicStatContext withMagicPercent(int value) {
            return new MagicStatContext(flatMainSum, mainStatMultiplier, mapleWarriorBonus, weaponMagic, equipMagic, value, buffMagic, skillCoeff);
        }
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
                    req.isBabyDragonFood(),
                    req.isHontaleVictory()
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
                    req.isBabyDragonFood(),
                    req.isHontaleVictory()
            );
        }

        static BuffBonus from(StatConvRequest req) {
            return from(
                    req.getBlessingLevel(),
                    req.getEmpressBlessingLevel(),
                    req.getMapleWarriorLevel(),
                    req.getMeditationLevel(),
                    req.getRageLevel(),
                    0,
                    false,
                    req.isBabyDragonFood(),
                    req.isHontaleVictory()
            );
        }

        private static BuffBonus from(int spiritLevel, int empressLevel, int mapleWarriorLevel,
                                      int meditationLevel, int rageLevel, int sharpEyesLevel,
                                      boolean decentSharpEyes, boolean babyDragonFood, boolean hontaleVictory) {
            int spirit = clamp(spiritLevel, 0, SPIRIT_BLESSING_MAX_LEVEL);
            int empress = clamp(empressLevel, 0, EMPRESS_BLESSING_MAX_LEVEL);
            int sharpEyes = clamp(sharpEyesLevel, 0, SHARP_EYES_MAX_LEVEL);
            int spiritAttack = spirit;
            int empressAttack = empress;
            int attack = spiritAttack
                    + empressAttack
                    + rageAttack(clamp(rageLevel, 0, RAGE_MAX_LEVEL))
                    + (babyDragonFood ? 7 : 0)
                    + (hontaleVictory ? 30 : 0);
            int magicBuff = Math.max(
                    clamp(meditationLevel, 0, MEDITATION_MAX_LEVEL),
                    hontaleVictory ? 40 : 0
            );
            int magic = spirit * 2
                    + empress * 2
                    + magicBuff;
            // ??0 = +140%p ?덈?媛?(?⑹뿰??, ?좏삎 蹂닿컙
            int critRate = sharpEyes == 0 ? 0 : (int) Math.floor((sharpEyes + 1) / 2.0);
            int critDmgPP = sharpEyes == 0 ? 0 : (int) Math.round(sharpEyes * 140.0 / 30.0);

            if (decentSharpEyes) {
                critRate += 10;
                critDmgPP += 115; // ?몄꺘 = +115%p ?덈?媛?(?⑹뿰??
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
