package io.github.jaemo_lim.mapleplanet_spring.domain.item.entity;

import io.github.jaemo_lim.mapleplanet_spring.global.enums.StatType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "item_base_stat")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemBaseStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Item item;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private StatType statType;

    @Column(nullable = false)
    private int value;
}
