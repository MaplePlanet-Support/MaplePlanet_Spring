package io.github.jaemo_lim.mapleplanet_spring.domain.simulator.maker.entity;

import io.github.jaemo_lim.mapleplanet_spring.domain.item.entity.Item;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "maker_recipe")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MakerRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Item resultItem;

    @Column(nullable = false)
    private long baseMesoCost;

    @Column(nullable = false)
    private int requiredLevel;

    @Builder.Default
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MakerMaterial> materials = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MakerResultOption> resultOptions = new ArrayList<>();
}
