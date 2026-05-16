package io.github.jaemo_lim.mapleplanet_spring.domain.preset.repository;

import io.github.jaemo_lim.mapleplanet_spring.domain.preset.entity.CharacterPreset;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CharacterPresetRepository extends JpaRepository<CharacterPreset, Long> {
    Optional<CharacterPreset> findByNickname(String nickname);
}
