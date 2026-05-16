package io.github.jaemo_lim.mapleplanet_spring.domain.monster.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "monster")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Monster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private int level;

    @Column(nullable = false)
    private long hp;

    // 방어율 (0~100%)
    @Column(nullable = false)
    private int defensePercent;

    // 속성 (무속성이면 null). ex) 불속성, 얼음속성
    @Column(length = 20)
    private String element;

    @Column(nullable = false, length = 50)
    private String mapName;
}
