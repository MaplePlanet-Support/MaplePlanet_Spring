package io.github.jaemo_lim.mapleplanet_spring.domain.preset.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PresetResetPasswordRequest {

    @NotBlank
    @Size(max = 30)
    private String nickname;

    @Size(max = 30)
    private String currentPassword;
}
