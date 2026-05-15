package io.github.jaemo_lim.mapleplanet_spring.domain.simulator.maker.entity;

import io.github.jaemo_lim.mapleplanet_spring.domain.item.entity.Item;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "maker_material")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MakerMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private MakerRecipe recipe;

    @Column(nullable = false, length = 100)
    private String materialName;

    @Column(nullable = false)
    private int quantity;

    // 아이템 DB에 있는 재료는 연결, 없으면 null
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = true)
    private Item materialItem;
}
