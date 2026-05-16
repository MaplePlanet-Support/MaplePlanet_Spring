package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.service;

import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto.DamageCalcRequest;
import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto.DamageCalcResponse;
import org.springframework.stereotype.Service;

@Service
public class DamageCalcService {

    // 정축/여축: 레벨 1당 공격력 +1
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
}
