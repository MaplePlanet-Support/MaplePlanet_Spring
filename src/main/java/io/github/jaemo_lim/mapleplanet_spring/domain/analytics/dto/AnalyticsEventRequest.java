package io.github.jaemo_lim.mapleplanet_spring.domain.analytics.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AnalyticsEventRequest {

    @NotBlank
    @Size(max = 64)
    private String visitorId;

    @NotBlank
    @Pattern(regexp = "PAGE_VIEW|PRESET_SAVE|PRESET_LOAD")
    private String eventType;

    @Size(max = 255)
    private String path;
}
