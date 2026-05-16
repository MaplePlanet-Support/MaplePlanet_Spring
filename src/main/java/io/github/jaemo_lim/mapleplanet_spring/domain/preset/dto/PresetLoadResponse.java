package io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PresetLoadResponse {
    private String nickname;
    private PresetConfigDto config;
}
