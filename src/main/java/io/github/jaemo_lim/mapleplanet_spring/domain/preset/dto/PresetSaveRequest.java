package io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PresetSaveRequest {

    @NotBlank
    private String nickname;

    private String password;

    private PresetConfigDto config;
}
