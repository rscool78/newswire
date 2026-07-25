package com.newswire.service;

import com.newswire.article.ArticleEntity;
import com.newswire.article.ArticleRepository;
import com.newswire.article.Category;
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
    private final ArticleClassifierService articleClassifierService;

    public ArticleStoreService(ArticleRepository repo, 
            SummarizationService summarizationService,
            ArticleClassifierService articleClassifierService) {

        this.repo = repo;
        this.summarizationService = summarizationService;
        this.articleClassifierService = articleClassifierService;
    }

    @Transactional
    public int saveIfNew(Iterable<NewsItem> items) {
        int inserted = 0;

        for (NewsItem x : items) {

            String title = x.title() == null ? "" : x.title();
            String url = x.url() == null ? "" : x.url();

            if (title.isBlank() || url.isBlank()) {
                continue;

            }
        

            String fp = FingerprintUtil.articleFingerprint(url, title);

            if (repo.existsByFingerprint(fp)) {
                continue;
            }

            Instant published = 
                (x.publishedAt() != null) 
                ? x.publishedAt() 
                : Instant.now();

            String normalizedSummary = 
                summarizationService.summarize(
                    x.title(), 
                    x.summary());

            // Classify the article using the title and normalized summary.
            Category detectedCategory =
                    articleClassifierService.classifyCategory(
                            title,
                            normalizedSummary,
                            x.category());

            try {
                repo.save(new ArticleEntity(
                        title,
                        url,
                        normalizedSummary,
                        x.sourceName(),
                        detectedCategory,
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