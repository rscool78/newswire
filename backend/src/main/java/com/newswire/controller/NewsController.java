package com.newswire.controller;

import com.newswire.article.ArticleRepository;
import com.newswire.article.Category;
import com.newswire.dto.NewsItemDto;
import com.newswire.dto.PageMeta;
import com.newswire.dto.PagedResponse;
import com.newswire.mapper.NewsItemMapper;
import com.newswire.service.ArticleStoreService;
import com.newswire.service.RssNewsService;
import com.newswire.service.RefreshStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class NewsController {

    private final RssNewsService rssNewsService;
    private final ArticleStoreService store;
    private final ArticleRepository repo;
    private final RefreshStatus refreshStatus;

    public NewsController(RssNewsService rssNewsService,
                          ArticleStoreService store,
                          ArticleRepository repo,
                          RefreshStatus refreshStatus) {
        this.rssNewsService = rssNewsService;
        this.store = store;
        this.repo = repo;
        this.refreshStatus = refreshStatus;
    }

    // DB-backed read (paged) — stable API contract
    @GetMapping("/api/news")
    public PagedResponse<NewsItemDto> news(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) Category category,
            @RequestParam(required = false) String source,
            @RequestParam(required = false) String q
    ) {
        var pageable = PageRequest.of(page, Math.min(size, 100));

        var results = repo.search(category, source, q, pageable);

        var dtos = results.getContent().stream()
                .map(NewsItemMapper::toDto)
                .toList();

        return new PagedResponse<>(
                dtos,
                new PageMeta(
                        results.getNumber(),
                        results.getSize(),
                        results.getTotalElements(),
                        results.getTotalPages()
                )
        );
    }

    @GetMapping("/api/news/status")
    public Map<String, Object> status() {
        Map<String, Object> m = new java.util.LinkedHashMap<>();
        m.put("lastRun", refreshStatus.getLastRun() == null ? null : refreshStatus.getLastRun().toString());
        m.put("lastSuccess", refreshStatus.getLastSuccess() == null ? null : refreshStatus.getLastSuccess().toString());
        m.put("lastError", refreshStatus.getLastError());
        return m;
    }

    // Manual ingestion: RSS -> DB
    @PostMapping("/api/news/refresh")
    public String refresh() {
        refreshStatus.markRun();
        try {
            // leaving this as your existing dto type (NewsItem) for ingestion path
            // you can later migrate ingestion to its own IngestNewsItemDto if desired
            List<com.newswire.dto.NewsItem> latest = rssNewsService.fetchLatest(200);
            int inserted = store.saveIfNew(latest);
            refreshStatus.markSuccess();
            return "Inserted " + inserted + " new articles";
        } catch (Exception e) {
            refreshStatus.markError(e.getMessage());
            throw e;
        }
    }
}