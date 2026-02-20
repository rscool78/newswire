package com.newswire.service;

import com.newswire.article.ArticleEntity;
import com.newswire.article.ArticleRepository;
import com.newswire.article.Category;
import com.newswire.dto.NewsItemDto;
import com.newswire.dto.PageMeta;
import com.newswire.dto.PagedResponse;
import com.newswire.mapper.NewsItemMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsQueryService {

    private final ArticleRepository repo;

    public NewsQueryService(ArticleRepository repo) {
        this.repo = repo;
    }

    public PagedResponse<NewsItemDto> getNews(Category category, Pageable pageable) {
        Page<ArticleEntity> page;

        if (category == null) {
            page = repo.findAllByOrderByPublishedAtDesc(pageable);
        } else {
            page = repo.findByCategoryOrderByPublishedAtDesc(category, pageable);
        }

        List<NewsItemDto> items = page.getContent().stream()
                .map(NewsItemMapper::toDto)
                .toList();

        PageMeta meta = new PageMeta(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );

        return new PagedResponse<>(items, meta);
    }
}