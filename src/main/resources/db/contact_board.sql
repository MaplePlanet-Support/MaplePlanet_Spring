CREATE TABLE contact_post (
    id BIGINT NOT NULL AUTO_INCREMENT,
    title VARCHAR(120) NOT NULL,
    content TEXT NOT NULL,
    nickname VARCHAR(30) NOT NULL,
    secret BIT NOT NULL,
    author_code_hash VARCHAR(64),
    preset_attached BIT NOT NULL,
    preset_nickname VARCHAR(30),
    preset_config_json TEXT,
    created_at DATETIME(6) NOT NULL,
    answer_content TEXT,
    answered_at DATETIME(6),
    PRIMARY KEY (id),
    INDEX idx_contact_post_created_at (created_at)
);

CREATE TABLE contact_attachment (
    id BIGINT NOT NULL AUTO_INCREMENT,
    post_id BIGINT NOT NULL,
    original_filename VARCHAR(255) NOT NULL,
    stored_filename VARCHAR(255) NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    size_bytes BIGINT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_contact_attachment_post_id (post_id)
);
