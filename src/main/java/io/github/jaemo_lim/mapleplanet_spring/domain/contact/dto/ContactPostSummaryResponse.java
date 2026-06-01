package io.github.jaemo_lim.mapleplanet_spring.domain.contact.dto;

import io.github.jaemo_lim.mapleplanet_spring.domain.contact.entity.ContactPost;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ContactPostSummaryResponse {
    private Long id;
    private String title;
    private String nickname;
    private boolean secret;
    private boolean presetAttached;
    private boolean readable;
    private boolean answered;
    private LocalDateTime createdAt;

    public static ContactPostSummaryResponse from(ContactPost post) {
        return new ContactPostSummaryResponse(
                post.getId(),
                post.getTitle(),
                post.getNickname(),
                post.isSecret(),
                post.isPresetAttached(),
                !post.isSecret(),
                post.getAnsweredAt() != null,
                post.getCreatedAt()
        );
    }
}
