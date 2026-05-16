package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class StatCompareRequest {

    @Min(0)
    private int baseStat;           // 직접 찍은 주스탯

    @Valid
    private List<ItemStatDto> equippedItems;  // 현재 착용 중인 전체 아이템 스탯 목록

    @Valid
    private ItemStatDto currentSlotItem;      // 비교 슬롯에 현재 착용된 아이템 (빈 슬롯이면 null)

    @NotNull @Valid
    private ItemStatDto itemA;

    @NotNull @Valid
    private ItemStatDto itemB;
}
