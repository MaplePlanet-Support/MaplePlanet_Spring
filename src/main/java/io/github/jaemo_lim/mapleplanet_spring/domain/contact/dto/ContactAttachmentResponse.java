package io.github.jaemo_lim.mapleplanet_spring.domain.contact.dto;

import io.github.jaemo_lim.mapleplanet_spring.domain.contact.entity.ContactAttachment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ContactAttachmentResponse {
    private Long id;
    private String filename;
    private String contentType;
    private long sizeBytes;
    private String downloadUrl;

    public static ContactAttachmentResponse from(ContactAttachment attachment) {
        return new ContactAttachmentResponse(
                attachment.getId(),
                attachment.getOriginalFilename(),
                attachment.getContentType(),
                attachment.getSizeBytes(),
                "/api/v1/contact/attachments/" + attachment.getId()
        );
    }
}
