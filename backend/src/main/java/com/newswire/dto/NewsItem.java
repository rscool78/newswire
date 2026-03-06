package com.newswire.dto;

import com.newswire.article.Category;

import java.time.Instant;

public record NewsItem(
        String title,
        String url,
        String sourceName,
        Category category,
        String summary,
        Instant publishedAt
) {}

//public record NewsItem(String title, String url) {}
