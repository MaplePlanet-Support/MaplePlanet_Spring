package io.github.jaemo_lim.mapleplanet_spring.domain.analytics.repository;

import io.github.jaemo_lim.mapleplanet_spring.domain.analytics.entity.AnalyticsEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface AnalyticsEventRepository extends JpaRepository<AnalyticsEvent, Long> {

    @Query("""
            select count(distinct e.visitorId)
            from AnalyticsEvent e
            where e.createdAt >= :from and e.createdAt < :to
            """)
    long countDistinctVisitorsBetween(@Param("from") LocalDateTime from, @Param("to") LocalDateTime to);
}
