package com.newswire.article;

import java.time.Instant;

// Normalize artile format. Java 21 record: simple, immutable

public record Article(
        String title,
        String url,
        String summary,
        String sourceName,
        String category,
        Instant publishedAt,
        String fingerprint
) {}
