package com.newswire.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 * Service layer:
 * - Contains "application/business" logic (even if light for now)
 * - Keeps controllers thin
 * - Translates Entities -> DTOs
 */
@Service
public class ArticleService {

    private final ArticleRepository repo;

    public ArticleService(ArticleRepository repo) {
        this.repo = repo;
    }

    /**
     * Latest articles across all categories (paged).
     */
    public Page<ArticleDto> getLatest(Pageable pageable) {
        return repo.findAllByOrderByPublishedAtDesc(pageable)
                   .map(this::toDto);
    }

    /**
     * Latest articles for a specific category (paged).
     */
    public Page<ArticleDto> getByCategory(Category category, Pageable pageable) {
        return repo.findByCategoryOrderByPublishedAtDesc(category, pageable)
                   .map(this::toDto);
    }

    /**
     * Entity -> DTO mapping.
     * We control what the API exposes here.
     */
    private ArticleDto toDto(ArticleEntity e) {
        return new ArticleDto(
                e.getId(),
                e.getTitle(),
                e.getUrl(),
                e.getSummary(),
                e.getSourceName(),
                e.getCategory(),
                e.getPublishedAt()
        );
    }
}


