package io.github.jaemo_lim.mapleplanet_spring.domain.contact.dto;

import io.github.jaemo_lim.mapleplanet_spring.domain.contact.entity.ContactAttachment;
import io.github.jaemo_lim.mapleplanet_spring.domain.contact.entity.ContactPost;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class ContactPostDetailResponse {
    private Long id;
    private String title;
    private String content;
    private String nickname;
    private boolean secret;
    private boolean presetAttached;
    private String presetNickname;
    private String presetConfigJson;
    private LocalDateTime createdAt;
    private String answerContent;
    private LocalDateTime answeredAt;
    private List<ContactAttachmentResponse> attachments;

    public static ContactPostDetailResponse from(ContactPost post, List<ContactAttachment> attachments) {
        return new ContactPostDetailResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getNickname(),
                post.isSecret(),
                post.isPresetAttached(),
                post.getPresetNickname(),
                post.getPresetConfigJson(),
                post.getCreatedAt(),
                post.getAnswerContent(),
                post.getAnsweredAt(),
                attachments.stream().map(ContactAttachmentResponse::from).toList()
        );
    }
}
