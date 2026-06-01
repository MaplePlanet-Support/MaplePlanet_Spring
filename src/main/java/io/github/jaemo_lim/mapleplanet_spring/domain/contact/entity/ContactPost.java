package io.github.jaemo_lim.mapleplanet_spring.domain.contact.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "contact_post")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContactPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String title;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false, length = 30)
    private String nickname;

    @Column(nullable = false)
    private boolean secret;

    @Column(length = 64)
    private String authorCodeHash;

    @Column(nullable = false)
    private boolean presetAttached;

    @Column(length = 30)
    private String presetNickname;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String presetConfigJson;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String answerContent;

    private LocalDateTime answeredAt;

    public static ContactPost create(
            String title,
            String content,
            String nickname,
            boolean secret,
            String authorCodeHash,
            String presetConfigJson
    ) {
        ContactPost post = new ContactPost();
        post.title = title;
        post.content = content;
        post.nickname = nickname;
        post.secret = secret;
        post.authorCodeHash = authorCodeHash;
        post.presetAttached = presetConfigJson != null;
        post.presetNickname = presetConfigJson == null ? null : nickname;
        post.presetConfigJson = presetConfigJson;
        return post;
    }

    public boolean authorMatches(String nickname, String authorCodeHash) {
        return this.nickname.equals(nickname) && this.authorCodeHash != null && this.authorCodeHash.equals(authorCodeHash);
    }

    public void answer(String answerContent) {
        this.answerContent = answerContent;
        this.answeredAt = LocalDateTime.now();
    }

    @PrePersist
    private void prePersist() {
        createdAt = LocalDateTime.now();
    }
}
