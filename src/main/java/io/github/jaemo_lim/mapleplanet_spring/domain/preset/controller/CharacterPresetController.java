package io.github.jaemo_lim.mapleplanet_spring.domain.preset.controller;

import io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto.PresetLoadRequest;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto.PresetLoadResponse;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto.PresetResetPasswordRequest;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto.PresetSaveRequest;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto.PresetSaveResponse;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.service.CharacterPresetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/preset")
@RequiredArgsConstructor
public class CharacterPresetController {

    private final CharacterPresetService presetService;

    @PostMapping("/save")
    public PresetSaveResponse save(@Valid @RequestBody PresetSaveRequest request) {
        return presetService.save(request);
    }

    @PostMapping("/load")
    public PresetLoadResponse load(@Valid @RequestBody PresetLoadRequest request) {
        return presetService.load(request);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@Valid @RequestBody PresetResetPasswordRequest request) {
        presetService.resetPassword(request);
        return ResponseEntity.ok().build();
    }
}
