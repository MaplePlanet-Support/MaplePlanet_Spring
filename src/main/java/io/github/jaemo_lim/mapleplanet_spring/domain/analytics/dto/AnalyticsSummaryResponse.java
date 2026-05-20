package io.github.jaemo_lim.mapleplanet_spring.domain.analytics.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class AnalyticsSummaryResponse {
    private LocalDate date;
    private long dau;
    private long mau;
}
