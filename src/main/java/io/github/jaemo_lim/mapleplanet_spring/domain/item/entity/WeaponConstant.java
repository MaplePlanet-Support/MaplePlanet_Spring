package io.github.jaemo_lim.mapleplanet_spring.domain.item.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "weapon_constant")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class WeaponConstant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String weaponType;

    // 스공 공식에 사용: (주스탯 × constant + 부스탯) × 총공격력 / 100
    @Column(nullable = false)
    private Double constant;
}
