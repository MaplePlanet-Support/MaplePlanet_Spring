package io.github.jaemo_lim.mapleplanet_spring.domain.simulator.scroll.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "scroll_probability")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ScrollProbability {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 30)
    private String scrollType;

    // -5 ~ +5
    @Column(nullable = false)
    private int statChange;

    @Column(nullable = false, precision = 6, scale = 5)
    private BigDecimal probability;
}
