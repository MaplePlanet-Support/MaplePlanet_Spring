package io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PresetSaveResponse {
    private String nickname;
    private LocalDateTime savedAt;
}
