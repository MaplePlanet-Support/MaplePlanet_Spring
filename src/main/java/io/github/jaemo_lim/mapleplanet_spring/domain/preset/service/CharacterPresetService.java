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

    private final CharacterPresetRepository presetRepository;
    private final ObjectMapper objectMapper;

    @Transactional
    public PresetSaveResponse save(PresetSaveRequest request) {
        String configJson = toJson(request.getConfig());
        Optional<CharacterPreset> existing = presetRepository.findByNickname(request.getNickname());

        CharacterPreset preset;
        if (existing.isEmpty()) {
            preset = CharacterPreset.create(request.getNickname(), request.getPassword(), configJson);
            presetRepository.save(preset);
        } else {
            preset = existing.get();
            validatePassword(preset, request.getPassword());
            preset.updateConfig(configJson, request.getPassword());
        }

        return new PresetSaveResponse(preset.getNickname(), preset.getSavedAt());
    }

    @Transactional(readOnly = true)
    public PresetLoadResponse load(PresetLoadRequest request) {
        CharacterPreset preset = presetRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new RuntimeException("프리셋을 찾을 수 없습니다"));

        validatePassword(preset, request.getPassword());

        PresetConfigDto config = fromJson(preset.getConfigJson());
        return new PresetLoadResponse(preset.getNickname(), config);
    }

    private void validatePassword(CharacterPreset preset, String inputPassword) {
        if (preset.getPassword() == null || preset.getPassword().isBlank()) {
            return;
        }
        if (!preset.getPassword().equals(inputPassword)) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다");
        }
    }

    private String toJson(PresetConfigDto config) {
        try {
            return objectMapper.writeValueAsString(config);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("설정 데이터 직렬화에 실패했습니다", e);
        }
    }

    private PresetConfigDto fromJson(String json) {
        try {
            return objectMapper.readValue(json, PresetConfigDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("설정 데이터 역직렬화에 실패했습니다", e);
        }
    }
}
