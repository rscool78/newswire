package com.newswire.source;

import com.newswire.article.Category;

public record FeedSource(
        String url,
        String sourceName,
        Category category
) {}
