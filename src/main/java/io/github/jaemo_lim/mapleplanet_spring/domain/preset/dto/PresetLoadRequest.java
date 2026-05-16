package io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PresetLoadRequest {

    @NotBlank
    private String nickname;

    private String password;
}
