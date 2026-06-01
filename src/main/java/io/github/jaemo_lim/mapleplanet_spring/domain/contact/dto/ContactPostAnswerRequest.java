package io.github.jaemo_lim.mapleplanet_spring.domain.contact.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactPostAnswerRequest {

    @NotBlank(message = "답변 내용을 입력해주세요.")
    @Size(max = 5000, message = "답변은 5000자 이하여야 합니다.")
    private String answerContent;
}
