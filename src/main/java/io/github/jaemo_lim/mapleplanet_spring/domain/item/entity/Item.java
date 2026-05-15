package io.github.jaemo_lim.mapleplanet_spring.domain.item.entity;

import io.github.jaemo_lim.mapleplanet_spring.global.enums.ItemGrade;
import io.github.jaemo_lim.mapleplanet_spring.global.enums.ItemType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "item")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ItemType itemType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ItemGrade grade;

    @Column(nullable = false)
    private int requiredLevel;

    // null = 공용 아이템
    @Column(length = 20)
    private String jobType;

    @Builder.Default
    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemBaseStat> baseStats = new ArrayList<>();
}
