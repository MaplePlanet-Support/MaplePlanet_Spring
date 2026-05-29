package io.github.jaemo_lim.mapleplanet_spring.domain.contact.repository;

import io.github.jaemo_lim.mapleplanet_spring.domain.contact.entity.ContactPost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactPostRepository extends JpaRepository<ContactPost, Long> {
    List<ContactPost> findTop100ByOrderByCreatedAtDesc();
}
