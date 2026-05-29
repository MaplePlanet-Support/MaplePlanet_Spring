package io.github.jaemo_lim.mapleplanet_spring.domain.contact.controller;

import io.github.jaemo_lim.mapleplanet_spring.domain.contact.dto.ContactPostCreateResponse;
import io.github.jaemo_lim.mapleplanet_spring.domain.contact.dto.ContactPostDetailResponse;
import io.github.jaemo_lim.mapleplanet_spring.domain.contact.dto.ContactPostSummaryResponse;
import io.github.jaemo_lim.mapleplanet_spring.domain.contact.service.ContactPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/v1/contact")
@RequiredArgsConstructor
public class ContactPostController {

    private final ContactPostService contactPostService;

    @PostMapping(value = "/posts", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ContactPostCreateResponse create(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam String nickname,
            @RequestParam(required = false) String presetCode,
            @RequestParam(defaultValue = "true") boolean secret,
            @RequestPart(required = false) List<MultipartFile> images
    ) {
        return contactPostService.create(title, content, nickname, presetCode, secret, images);
    }

    @GetMapping("/posts")
    public List<ContactPostSummaryResponse> publicList() {
        return contactPostService.publicList();
    }

    @GetMapping("/posts/{id}")
    public ContactPostDetailResponse publicDetail(
            @PathVariable Long id,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String presetCode
    ) {
        return contactPostService.publicDetail(id, nickname, presetCode);
    }

    @GetMapping("/attachments/{id}")
    public ResponseEntity<?> publicAttachment(
            @PathVariable Long id,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String presetCode
    ) {
        ContactPostService.AttachmentResource attachment = contactPostService.publicAttachment(id, nickname, presetCode);
        return attachmentResponse(attachment);
    }

    @GetMapping("/admin/posts")
    public List<ContactPostSummaryResponse> adminList(
            @RequestHeader(value = "X-Analytics-Key", required = false) String adminKey
    ) {
        return contactPostService.adminList(adminKey);
    }

    @GetMapping("/admin/posts/{id}")
    public ContactPostDetailResponse adminDetail(
            @PathVariable Long id,
            @RequestHeader(value = "X-Analytics-Key", required = false) String adminKey
    ) {
        return contactPostService.adminDetail(id, adminKey);
    }

    @GetMapping("/admin/attachments/{id}")
    public ResponseEntity<?> adminAttachment(
            @PathVariable Long id,
            @RequestHeader(value = "X-Analytics-Key", required = false) String adminKey
    ) {
        ContactPostService.AttachmentResource attachment = contactPostService.adminAttachment(id, adminKey);
        return attachmentResponse(attachment);
    }

    private ResponseEntity<?> attachmentResponse(ContactPostService.AttachmentResource attachment) {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(attachment.contentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline()
                        .filename(attachment.filename(), StandardCharsets.UTF_8)
                        .build()
                        .toString())
                .body(attachment.resource());
    }
}
