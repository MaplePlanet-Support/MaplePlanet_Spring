package io.github.jaemo_lim.mapleplanet_spring.domain.character.entity;

import io.github.jaemo_lim.mapleplanet_spring.domain.item.entity.WeaponConstant;
import io.github.jaemo_lim.mapleplanet_spring.domain.job.entity.Job;
import io.github.jaemo_lim.mapleplanet_spring.domain.user.entity.UserProfile;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "characters")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 비로그인 사용자는 null. 프로필 저장 시 연결
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "weapon_constant_id", nullable = false)
    private WeaponConstant weaponConstant;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false)
    private int level;

    // 스탯 창에서 직접 찍은 주스탯 (스킬/아이템 제외)
    @Column(nullable = false)
    private int baseMainStat;

    // 스탯 창에서 직접 찍은 부스탯
    @Column(nullable = false)
    private int baseSubStat;

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<EquippedItem> equippedItems = new ArrayList<>();

    @OneToMany(mappedBy = "character", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<CharacterSkill> skills = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
