package io.github.jaemo_lim.mapleplanet_spring.domain.preset.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto.PresetConfigDto;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto.PresetLoadRequest;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto.PresetLoadResponse;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto.PresetResetPasswordRequest;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto.PresetSaveRequest;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto.PresetSaveResponse;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.entity.CharacterPreset;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.repository.CharacterPresetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
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
        if (request.getPassword() == null || request.getPassword().isBlank()) {
            throw new RuntimeException("비밀번호를 설정해주세요.");
        }
        String newHash = hashPassword(request.getPassword());

        Optional<CharacterPreset> existing = presetRepository.findByNickname(request.getNickname());
        CharacterPreset preset;
        if (existing.isEmpty()) {
            preset = CharacterPreset.create(request.getNickname(), configJson, newHash);
            presetRepository.save(preset);
        } else {
            preset = existing.get();
            if (preset.hasPassword() && !preset.passwordMatches(hashPassword(request.getPassword()))) {
                throw new RuntimeException("비밀번호가 틀렸습니다.");
            }
            preset.updateConfig(configJson, newHash);
            presetRepository.save(preset);
        }

        return new PresetSaveResponse(preset.getNickname(), preset.getSavedAt());
    }

    @Transactional(readOnly = true)
    public PresetLoadResponse load(PresetLoadRequest request) {
        CharacterPreset preset = presetRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new RuntimeException("해당 닉네임을 찾을 수 없습니다."));

        if (preset.hasPassword() && !preset.passwordMatches(hashPassword(request.getPassword()))) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

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

    @Transactional
    public void resetPassword(PresetResetPasswordRequest request) {
        CharacterPreset preset = presetRepository.findByNickname(request.getNickname())
                .orElseThrow(() -> new RuntimeException("해당 닉네임을 찾을 수 없습니다."));

        if (preset.hasPassword() && !preset.passwordMatches(hashPassword(request.getCurrentPassword()))) {
            throw new RuntimeException("비밀번호가 틀렸습니다.");
        }

        preset.updateConfig(preset.getConfigJson(), null);
    }

    private String hashPassword(String password) {
        if (password == null || password.isBlank()) return null;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password.", e);
        }
    }
}
