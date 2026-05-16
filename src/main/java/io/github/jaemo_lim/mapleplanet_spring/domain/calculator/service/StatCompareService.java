package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.service;

import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto.ItemStatDto;
import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto.StatCompareRequest;
import io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto.StatCompareResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatCompareService {

    public StatCompareResponse compare(StatCompareRequest req) {
        int totalFlat = sumFlat(req.getEquippedItems());
        int totalPercent = sumPercent(req.getEquippedItems());

        // 비교 슬롯에 기존 아이템이 있으면 제거
        if (req.getCurrentSlotItem() != null) {
            totalFlat -= req.getCurrentSlotItem().getFlatStat();
            totalPercent -= req.getCurrentSlotItem().getStatPercent();
        }

        double statA = calculate(req.getBaseStat(), totalFlat, totalPercent, req.getItemA());
        double statB = calculate(req.getBaseStat(), totalFlat, totalPercent, req.getItemB());

        double diff = statB - statA;
        String betterItem = diff > 0 ? "B" : diff < 0 ? "A" : "SAME";

        return new StatCompareResponse(statA, statB, diff, betterItem);
    }

    private double calculate(int baseStat, int totalFlat, int totalPercent, ItemStatDto item) {
        return (baseStat + totalFlat + item.getFlatStat())
                * (1 + (totalPercent + item.getStatPercent()) / 100.0);
    }

    private int sumFlat(List<ItemStatDto> items) {
        if (items == null) return 0;
        return items.stream().mapToInt(ItemStatDto::getFlatStat).sum();
    }

    private int sumPercent(List<ItemStatDto> items) {
        if (items == null) return 0;
        return items.stream().mapToInt(ItemStatDto::getStatPercent).sum();
    }
}
