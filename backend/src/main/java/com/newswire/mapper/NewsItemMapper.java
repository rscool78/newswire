package com.newswire.mapper;

import com.newswire.article.ArticleEntity;
import com.newswire.dto.NewsItemDto;

public final class NewsItemMapper {

    private NewsItemMapper() {}

    public static NewsItemDto toDto(ArticleEntity a) {
        return new NewsItemDto(
                a.getId(),
                a.getTitle(),
                a.getUrl(),
                a.getSourceName(),
                a.getCategory(),
                a.getSummary(),
                a.getPublishedAt()
        );
    }
}