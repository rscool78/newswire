package com.newswire.dto;

import com.newswire.article.Category;
import java.time.Instant;

public record NewsItemDto(
        long id,
        String title,
        String url,          // ← url (not link)
        String sourceName,   // ← sourceName (not source)
        Category category,
        String summary,
        Instant publishedAt
) {}