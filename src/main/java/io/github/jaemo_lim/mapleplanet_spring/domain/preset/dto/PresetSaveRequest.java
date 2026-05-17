package io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PresetSaveRequest {

    @NotBlank
    @Size(max = 30)
    private String nickname;

    @NotNull
    @Valid
    private PresetConfigDto config;
}
