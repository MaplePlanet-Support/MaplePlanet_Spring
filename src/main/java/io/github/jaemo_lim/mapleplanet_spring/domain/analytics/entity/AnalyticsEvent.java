package io.github.jaemo_lim.mapleplanet_spring.domain.analytics.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "analytics_event")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnalyticsEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String visitorId;

    @Column(nullable = false, length = 40)
    private String eventType;

    @Column(length = 255)
    private String path;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public static AnalyticsEvent create(String visitorId, String eventType, String path) {
        AnalyticsEvent event = new AnalyticsEvent();
        event.visitorId = visitorId;
        event.eventType = eventType;
        event.path = path;
        return event;
    }

    @PrePersist
    private void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
