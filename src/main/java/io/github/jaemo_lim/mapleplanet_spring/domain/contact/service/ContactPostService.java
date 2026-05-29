package io.github.jaemo_lim.mapleplanet_spring.domain.contact.service;

import io.github.jaemo_lim.mapleplanet_spring.domain.contact.dto.ContactPostCreateResponse;
import io.github.jaemo_lim.mapleplanet_spring.domain.contact.dto.ContactPostDetailResponse;
import io.github.jaemo_lim.mapleplanet_spring.domain.contact.dto.ContactPostSummaryResponse;
import io.github.jaemo_lim.mapleplanet_spring.domain.contact.entity.ContactAttachment;
import io.github.jaemo_lim.mapleplanet_spring.domain.contact.entity.ContactPost;
import io.github.jaemo_lim.mapleplanet_spring.domain.contact.repository.ContactAttachmentRepository;
import io.github.jaemo_lim.mapleplanet_spring.domain.contact.repository.ContactPostRepository;
import io.github.jaemo_lim.mapleplanet_spring.domain.preset.service.CharacterPresetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactPostService {

    private static final int MAX_TITLE_LENGTH = 120;
    private static final int MAX_CONTENT_LENGTH = 5_000;
    private static final int MAX_NICKNAME_LENGTH = 30;
    private static final int MAX_ATTACHMENTS = 10;
    private static final long MAX_ATTACHMENT_BYTES = 3L * 1024 * 1024;
    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            "image/jpeg",
            "image/png",
            "image/webp",
            "image/gif"
    );

    private final ContactPostRepository postRepository;
    private final ContactAttachmentRepository attachmentRepository;
    private final CharacterPresetService presetService;

    @Value("${app.contact.upload-dir:uploads/contact}")
    private String uploadDir;

    @Value("${app.contact.admin-key:}")
    private String contactAdminKey;

    @Value("${app.analytics.admin-key:}")
    private String analyticsAdminKey;

    @Transactional
    public ContactPostCreateResponse create(
            String title,
            String content,
            String nickname,
            String presetCode,
            boolean secret,
            List<MultipartFile> images
    ) {
        String normalizedTitle = requireText(title, "제목", MAX_TITLE_LENGTH);
        String normalizedContent = requireText(content, "내용", MAX_CONTENT_LENGTH);
        String normalizedNickname = requireText(nickname, "닉네임", MAX_NICKNAME_LENGTH);
        String presetConfigJson = null;
        String authorCodeHash = null;

        if (presetCode != null && !presetCode.isBlank()) {
            presetConfigJson = presetService.verifiedConfigJson(normalizedNickname, presetCode);
            authorCodeHash = hashCode(presetCode);
        }

        if (secret && authorCodeHash == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "비밀글은 저장 코드를 입력해야 합니다.");
        }

        ContactPost post = postRepository.save(ContactPost.create(
                normalizedTitle,
                normalizedContent,
                normalizedNickname,
                secret,
                authorCodeHash,
                presetConfigJson
        ));

        for (MultipartFile image : safeImages(images)) {
            ContactAttachment attachment = storeAttachment(post.getId(), image);
            attachmentRepository.save(attachment);
        }

        return new ContactPostCreateResponse(post.getId(), post.isPresetAttached());
    }

    @Transactional(readOnly = true)
    public List<ContactPostSummaryResponse> adminList(String providedKey) {
        validateAdmin(providedKey);
        return postRepository.findTop100ByOrderByCreatedAtDesc().stream()
                .map(ContactPostSummaryResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ContactPostDetailResponse adminDetail(Long id, String providedKey) {
        validateAdmin(providedKey);
        ContactPost post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact post not found."));
        List<ContactAttachment> attachments = attachmentRepository.findByPostIdOrderByIdAsc(id);
        return ContactPostDetailResponse.from(post, attachments);
    }

    @Transactional(readOnly = true)
    public List<ContactPostSummaryResponse> publicList() {
        return postRepository.findTop100ByOrderByCreatedAtDesc().stream()
                .map(ContactPostSummaryResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ContactPostDetailResponse publicDetail(Long id, String nickname, String presetCode) {
        ContactPost post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact post not found."));
        if (post.isSecret()) {
            String normalizedNickname = nickname == null ? "" : nickname.trim();
            String normalizedCode = presetCode == null ? "" : presetCode.trim();
            if (normalizedNickname.isBlank() || normalizedCode.isBlank()
                    || !post.authorMatches(normalizedNickname, hashCode(normalizedCode))) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀글은 작성자만 볼 수 있습니다.");
            }
        }
        List<ContactAttachment> attachments = attachmentRepository.findByPostIdOrderByIdAsc(id);
        return ContactPostDetailResponse.from(post, attachments);
    }

    @Transactional(readOnly = true)
    public AttachmentResource adminAttachment(Long id, String providedKey) {
        validateAdmin(providedKey);
        ContactAttachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attachment not found."));
        return attachmentResource(attachment);
    }

    @Transactional(readOnly = true)
    public AttachmentResource publicAttachment(Long id, String nickname, String presetCode) {
        ContactAttachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Attachment not found."));
        ContactPost post = postRepository.findById(attachment.getPostId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact post not found."));
        if (post.isSecret()) {
            String normalizedNickname = nickname == null ? "" : nickname.trim();
            String normalizedCode = presetCode == null ? "" : presetCode.trim();
            if (normalizedNickname.isBlank() || normalizedCode.isBlank()
                    || !post.authorMatches(normalizedNickname, hashCode(normalizedCode))) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "비밀글 첨부는 작성자만 볼 수 있습니다.");
            }
        }
        return attachmentResource(attachment);
    }

    private AttachmentResource attachmentResource(ContactAttachment attachment) {
        Path path = uploadRoot().resolve(attachment.getStoredFilename()).normalize();
        if (!path.startsWith(uploadRoot()) || !Files.exists(path)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Attachment file not found.");
        }
        return new AttachmentResource(
                new FileSystemResource(path),
                attachment.getOriginalFilename(),
                attachment.getContentType()
        );
    }

    private String requireText(String value, String label, int maxLength) {
        String normalized = value == null ? "" : value.trim();
        if (normalized.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, label + "을 입력해주세요.");
        }
        if (normalized.length() > maxLength) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, label + "은 " + maxLength + "자 이하여야 합니다.");
        }
        return normalized;
    }

    private List<MultipartFile> safeImages(List<MultipartFile> images) {
        if (images == null) {
            return List.of();
        }
        List<MultipartFile> nonEmpty = images.stream()
                .filter(image -> image != null && !image.isEmpty())
                .toList();
        if (nonEmpty.size() > MAX_ATTACHMENTS) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미지는 최대 " + MAX_ATTACHMENTS + "개까지 첨부할 수 있습니다.");
        }
        return nonEmpty;
    }

    private ContactAttachment storeAttachment(Long postId, MultipartFile image) {
        String contentType = image.getContentType();
        if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase(Locale.ROOT))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "jpg, png, webp, gif 이미지만 첨부할 수 있습니다.");
        }
        if (image.getSize() > MAX_ATTACHMENT_BYTES) {
            throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "이미지는 파일당 3MB 이하여야 합니다.");
        }

        String original = StringUtils.cleanPath(image.getOriginalFilename() == null ? "image" : image.getOriginalFilename());
        String extension = extension(original);
        String stored = UUID.randomUUID() + extension;
        Path root = uploadRoot();
        Path target = root.resolve(stored).normalize();
        if (!target.startsWith(root)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid filename.");
        }

        try {
            Files.createDirectories(root);
            image.transferTo(target);
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장에 실패했습니다.", e);
        }

        return ContactAttachment.create(postId, original, stored, contentType, image.getSize());
    }

    private String extension(String filename) {
        int dot = filename.lastIndexOf('.');
        if (dot < 0 || dot == filename.length() - 1) {
            return "";
        }
        String ext = filename.substring(dot).toLowerCase(Locale.ROOT);
        return ext.length() > 12 ? "" : ext;
    }

    private Path uploadRoot() {
        return Path.of(uploadDir).toAbsolutePath().normalize();
    }

    private void validateAdmin(String providedKey) {
        String adminKey = contactAdminKey != null && !contactAdminKey.isBlank()
                ? contactAdminKey
                : analyticsAdminKey;
        if (adminKey != null && !adminKey.isBlank() && !adminKey.equals(providedKey)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Contact admin key is invalid.");
        }
    }

    private String hashCode(String code) {
        if (code == null || code.isBlank()) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(code.getBytes(StandardCharsets.UTF_8));
            return HexFormat.of().formatHex(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash contact code.", e);
        }
    }

    public record AttachmentResource(Resource resource, String filename, String contentType) {
    }
}
