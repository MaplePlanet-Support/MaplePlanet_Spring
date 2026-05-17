package io.github.jaemo_lim.mapleplanet_spring.domain.calculator.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class StatCompareRequest {

    @Min(0)
    private int baseStat;

    @Valid
    @Size(max = 32)
    private List<ItemStatDto> equippedItems;

    @Valid
    private ItemStatDto currentSlotItem;

    @NotNull
    @Valid
    private ItemStatDto itemA;

    @NotNull
    @Valid
    private ItemStatDto itemB;
}
