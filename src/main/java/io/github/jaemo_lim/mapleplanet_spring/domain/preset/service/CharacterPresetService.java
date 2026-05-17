package io.github.jaemo_lim.mapleplanet_spring.domain.preset.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto.PresetConfigDto;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto.PresetLoadRequest;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto.PresetLoadResponse;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto.PresetSaveRequest;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto.PresetSaveResponse;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.entity.CharacterPreset;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.repository.CharacterPresetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CharacterPresetService {

    private static final int MAX_CONFIG_JSON_LENGTH = 20_000;

    private final CharacterPresetRepository presetRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public PresetSaveResponse save(PresetSaveRequest request) {
        String configJson = toJson(request.getConfig());
        if (configJson.length() > MAX_CONFIG_JSON_LENGTH) {
            throw new RuntimeException("Preset config is too large.");
        }

        Optional<CharacterPreset> existing = presetRepository.findByNickname(request.getNickname());
        CharacterPreset preset;
        if (existing.isEmpty()) {
            preset = CharacterPreset.create(request.getNickname(), configJson);
            presetRepository.save(preset);
        } else {
            preset = existing.get();
            preset.updateConfig(configJson);
        }

        return new PresetSaveResponse(preset.getNickname(), preset.getSavedAt());
    }

    @Transactional(readOnly = true)
    public PresetLoadResponse load(PresetLoadRequest request) {
        CharacterPreset preset = presetRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new RuntimeException("Preset not found."));

        PresetConfigDto config = fromJson(preset.getConfigJson());
        return new PresetLoadResponse(preset.getNickname(), config);
    }

    private String toJson(PresetConfigDto config) {
        try {
            return objectMapper.writeValueAsString(config);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize preset config.", e);
        }
    }

    private PresetConfigDto fromJson(String json) {
        try {
            return objectMapper.readValue(json, PresetConfigDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to deserialize preset config.", e);
        }
    }
}
