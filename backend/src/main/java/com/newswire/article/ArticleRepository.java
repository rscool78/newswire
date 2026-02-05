package com.newswire.article;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {
    boolean existsByFingerprint(String fingerprint);
}
