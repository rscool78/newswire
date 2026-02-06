package com.newswire.article;

import java.time.Instant;

/**
 * DTO (Data Transfer Object) returned by the REST API.
 * We DO NOT return ArticleEntity directly so we don't leak DB concerns to clients.
 */
public record ArticleDto(
        Long id,
        String title,
        String url,
        String summary,
        String sourceName,
        Category category,
        Instant publishedAt
) {}
