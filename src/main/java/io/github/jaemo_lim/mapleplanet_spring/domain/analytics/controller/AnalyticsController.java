package io.github.jaemo_lim.mapleplanet_spring.domain.analytics.controller;

import io.github.jaemo_lim.mapleplanet_spring.domain.analytics.dto.AnalyticsEventRequest;
import io.github.jaemo_lim.mapleplanet_spring.domain.analytics.dto.AnalyticsSummaryResponse;
import io.github.jaemo_lim.mapleplanet_spring.domain.analytics.service.AnalyticsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @PostMapping("/events")
    public ResponseEntity<Void> record(@Valid @RequestBody AnalyticsEventRequest request) {
        analyticsService.record(request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary")
    public AnalyticsSummaryResponse summary(@RequestHeader(value = "X-Analytics-Key", required = false) String adminKey) {
        return analyticsService.summary(adminKey);
    }
}
