package io.github.jaemo_lim.mapleplanet_spring.domain.preset.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "character_preset")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CharacterPreset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 30)
    private String nickname;

    @Column(length = 100)
    private String password;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String configJson;

    @Column(nullable = false)
    private LocalDateTime savedAt;

    public static CharacterPreset create(String nickname, String password, String configJson) {
        CharacterPreset preset = new CharacterPreset();
        preset.nickname = nickname;
        preset.password = password;
        preset.configJson = configJson;
        return preset;
    }

    public void updateConfig(String configJson, String password) {
        this.configJson = configJson;
        this.password = password;
    }

    @PrePersist
    private void prePersist() {
        this.savedAt = LocalDateTime.now();
    }

    @PreUpdate
    private void preUpdate() {
        this.savedAt = LocalDateTime.now();
    }
}
