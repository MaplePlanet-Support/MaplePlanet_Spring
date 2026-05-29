package io.github.jaemo_lim.mapleplanet_spring.domain.contact.repository;

import io.github.jaemo_lim.mapleplanet_spring.domain.contact.entity.ContactAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactAttachmentRepository extends JpaRepository<ContactAttachment, Long> {
    List<ContactAttachment> findByPostIdOrderByIdAsc(Long postId);
}
