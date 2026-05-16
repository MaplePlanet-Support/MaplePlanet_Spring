package io.github.jaemo_lim.mapleplanet_spring.domain.character.entity;

import io.github.jaemo_lim.mapleplanet_spring.domain.skill.entity.Skill;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "character_skill")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CharacterSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "character_id", nullable = false)
    private Character character;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skill_id", nullable = false)
    private Skill skill;

    @Column(nullable = false)
    private int skillLevel;
}
