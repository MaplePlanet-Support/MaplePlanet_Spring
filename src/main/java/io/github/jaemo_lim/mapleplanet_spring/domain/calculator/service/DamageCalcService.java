package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.service;

import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DamageCalcService {

    private static final int BLESSING_ATK_PER_LEVEL = 1;

    public DamageCalcResponse calculate(DamageCalcRequest req) {
        int blessingAtk = req.getBlessingLevel() * BLESSING_ATK_PER_LEVEL;
        int empressBlessingAtk = req.getEmpressBlessingLevel() * BLESSING_ATK_PER_LEVEL;

        int baseAtk = req.getWeaponAtk() + req.getEquipAtk() + blessingAtk + empressBlessingAtk;
        int totalAtk = (int) Math.floor(baseAtk * (1 + req.getAtkPercent() / 100.0));

        double damage = (req.getMainStat() * req.getWeaponType().getConstant() + req.getSubStat())
                * totalAtk / 100.0;

        return new DamageCalcResponse(blessingAtk, empressBlessingAtk, totalAtk, damage);
    }

    public DamageCompareResponse compare(DamageCompareRequest req) {
        int blessingAtk = (req.getBlessingLevel() + req.getEmpressBlessingLevel()) * BLESSING_ATK_PER_LEVEL;

        int totalFlatStat = sum(req.getEquippedItems(), "flat");
        int totalStatPercent = sum(req.getEquippedItems(), "percent");
        int totalFlatAtk = sum(req.getEquippedItems(), "atk");

        if (req.getCurrentSlotItem() != null) {
            totalFlatStat -= req.getCurrentSlotItem().getFlatStat();
            totalStatPercent -= req.getCurrentSlotItem().getStatPercent();
            totalFlatAtk -= req.getCurrentSlotItem().getFlatAtk();
        }

        double statA = calcStat(req.getBaseStat(), totalFlatStat, totalStatPercent, req.getItemA());
        double statB = calcStat(req.getBaseStat(), totalFlatStat, totalStatPercent, req.getItemB());

        double dmgA = calcDamage(statA, req.getSubStat(), req.getWeaponType().getConstant(),
                req.getWeaponAtk(), totalFlatAtk, req.getAtkPercent(), blessingAtk, req.getItemA());
        double dmgB = calcDamage(statB, req.getSubStat(), req.getWeaponType().getConstant(),
                req.getWeaponAtk(), totalFlatAtk, req.getAtkPercent(), blessingAtk, req.getItemB());

        double dmgDiff = dmgB - dmgA;
        String better = dmgDiff > 0 ? "B" : dmgDiff < 0 ? "A" : "SAME";

        return new DamageCompareResponse(statA, statB, dmgA, dmgB, statB - statA, dmgDiff, better);
    }

    private double calcStat(int baseStat, int totalFlat, int totalPercent, ItemStatFullDto item) {
        return (baseStat + totalFlat + item.getFlatStat())
                * (1 + (totalPercent + item.getStatPercent()) / 100.0);
    }

    private double calcDamage(double mainStat, int subStat, double weaponConst,
                              int weaponAtk, int totalFlatAtk, int atkPercent,
                              int blessingAtk, ItemStatFullDto item) {
        int totalAtk = (int) Math.floor(
                (weaponAtk + totalFlatAtk + item.getFlatAtk() + blessingAtk) * (1 + atkPercent / 100.0)
        );
        return (mainStat * weaponConst + subStat) * totalAtk / 100.0;
    }

    private int sum(List<ItemStatFullDto> items, String type) {
        if (items == null) return 0;
        return switch (type) {
            case "flat" -> items.stream().mapToInt(ItemStatFullDto::getFlatStat).sum();
            case "percent" -> items.stream().mapToInt(ItemStatFullDto::getStatPercent).sum();
            case "atk" -> items.stream().mapToInt(ItemStatFullDto::getFlatAtk).sum();
            default -> 0;
        };
    }
}
