package io.github.jaemo_lim.mapleplanet_spring.domain.contact.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContactPostCreateResponse {
    private Long id;
    private boolean presetAttached;
}
