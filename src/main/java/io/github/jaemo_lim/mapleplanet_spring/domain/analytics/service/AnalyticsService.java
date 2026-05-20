package io.github.jaemo_lim.mapleplanet_spring.domain.analytics.service;

import io.github.jaemo_lim.mapleplanet_spring.domain.analytics.dto.AnalyticsEventRequest;
import io.github.jaemo_lim.mapleplanet_spring.domain.analytics.dto.AnalyticsSummaryResponse;
import io.github.jaemo_lim.mapleplanet_spring.domain.analytics.entity.AnalyticsEvent;
import io.github.jaemo_lim.mapleplanet_spring.domain.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsService {

    private static final ZoneId SERVICE_ZONE = ZoneId.of("Asia/Seoul");

    private final AnalyticsEventRepository analyticsEventRepository;

    @Value("${app.analytics.admin-key:}")
    private String adminKey;

    @Transactional
    public void record(AnalyticsEventRequest request) {
        String path = request.getPath() == null ? null : request.getPath().trim();
        String visitorId = request.getVisitorId().trim();
        analyticsEventRepository.save(AnalyticsEvent.create(
                visitorId,
                request.getEventType(),
                path == null || path.isBlank() ? null : path
        ));
        log.info("analytics event recorded: type={}, visitor={}, path={}",
                request.getEventType(),
                maskVisitorId(visitorId),
                path == null || path.isBlank() ? "-" : path);
    }

    @Transactional(readOnly = true)
    public AnalyticsSummaryResponse summary(String providedKey) {
        if (adminKey != null && !adminKey.isBlank() && !adminKey.equals(providedKey)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Analytics admin key is invalid.");
        }

        LocalDate today = LocalDate.now(SERVICE_ZONE);
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime tomorrowStart = today.plusDays(1).atStartOfDay();
        LocalDateTime mauStart = today.minusDays(29).atStartOfDay();

        long dau = analyticsEventRepository.countDistinctVisitorsBetween(todayStart, tomorrowStart);
        long mau = analyticsEventRepository.countDistinctVisitorsBetween(mauStart, tomorrowStart);

        log.info("analytics summary requested: date={}, dau={}, mau={}", today, dau, mau);
        return new AnalyticsSummaryResponse(today, dau, mau);
    }

    private String maskVisitorId(String visitorId) {
        if (visitorId.length() <= 8) {
            return visitorId;
        }
        return visitorId.substring(0, 8) + "...";
    }
}
