package com.newswire.service;

import com.newswire.article.ArticleEntity;
import com.newswire.article.ArticleRepository;
import com.newswire.dto.NewsItem;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.newswire.util.FingerprintUtil;

import java.time.Instant;

@Service
public class ArticleStoreService {

    private final ArticleRepository repo;
    private final SummarizationService summarizationService;

    public ArticleStoreService(ArticleRepository repo, SummarizationService summarizationService) {
        this.repo = repo;
        this.summarizationService = summarizationService;
    }

    @Transactional
    public int saveIfNew(Iterable<NewsItem> items) {
        int inserted = 0;

        for (NewsItem x : items) {
            String title = x.title() == null ? "" : x.title();
            String url = x.url() == null ? "" : x.url();

            if (title.isBlank() || url.isBlank()) continue;

            String fp = FingerprintUtil.articleFingerprint(url, title);

            if (repo.existsByFingerprint(fp)) continue;

            Instant published = (x.publishedAt() != null) ? x.publishedAt() : Instant.now();
            String normalizedSummary = summarizationService.summarize(x.title(), x.summary());

            try {
                repo.save(new ArticleEntity(
                        title,
                        url,
                        normalizedSummary,
                        x.sourceName(),
                        x.category(),
                        published,
                        fp
                ));
                inserted++;
            } catch (DataIntegrityViolationException dup) {
                // ignore duplicate insert race
            }
        }

        return inserted;
    }
    
}