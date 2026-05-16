package io.github.jaemo_lim.mapleplanet_spring.domain.character.entity;

import io.github.jaemo_lim.mapleplanet_spring.global.enums.EquipSlot;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "equipped_item")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EquippedItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private EquipSlot slot;

    @Column(nullable = false, length = 60)
    private String itemName;

    // 깡 스탯 (아이템 고정 수치)
    @Column(nullable = false)
    private int strFlat;

    @Column(nullable = false)
    private int dexFlat;

    @Column(nullable = false)
    private int intFlat;

    @Column(nullable = false)
    private int lukFlat;

    @Column(nullable = false)
    private int atkFlat;

    @Column(nullable = false)
    private int matkFlat;

    // % 스탯 (잠재능력 등)
    @Column(nullable = false)
    private int strPercent;

    @Column(nullable = false)
    private int dexPercent;

    @Column(nullable = false)
    private int intPercent;

    @Column(nullable = false)
    private int lukPercent;

    @Column(nullable = false)
    private int atkPercent;

    @Column(nullable = false)
    private int matkPercent;
}
