package io.github.jaemo_lim.mapleplanet_spring.domain.preset.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
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

    @Column(columnDefinition = "TEXT", nullable = false)
    private String configJson;

    @Column(length = 64)
    private String passwordHash;

    @Column(nullable = false)
    private LocalDateTime savedAt;

    public static CharacterPreset create(String nickname, String configJson, String passwordHash) {
        CharacterPreset preset = new CharacterPreset();
        preset.nickname = nickname;
        preset.configJson = configJson;
        preset.passwordHash = passwordHash;
        return preset;
    }

    public void updateConfig(String configJson, String passwordHash) {
        this.configJson = configJson;
        this.passwordHash = passwordHash;
    }

    public boolean hasPassword() {
        return passwordHash != null;
    }

    public boolean passwordMatches(String hash) {
        return passwordHash != null && passwordHash.equals(hash);
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
