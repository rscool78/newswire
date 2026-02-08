package com.newswire.service;

import com.newswire.article.ArticleEntity;
import com.newswire.article.ArticleRepository;
import com.newswire.dto.NewsItem;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class ArticleStoreService {

    private final ArticleRepository repo;

    public ArticleStoreService(ArticleRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public int saveIfNew(Iterable<NewsItem> items) {
        int inserted = 0;

        for (NewsItem x : items) {
            String fp = fingerprint(x.url(), x.title()); // matches your comment

            if (repo.existsByFingerprint(fp)) continue;

            repo.save(new ArticleEntity(
                    x.title(),
                    x.url(),
                    null,               // summary (we’ll populate later from RSS)
                    x.sourceName(),
                    x.category(),
                    x.publishedAt(),
                    fp
            ));
            inserted++;
        }

        return inserted;
    }

    // SHA-256 -> 64 hex chars (matches ArticleEntity fingerprint length=64)
    private String fingerprint(String url, String title) {
        String raw = (url == null ? "" : url.trim()) + "||" + (title == null ? "" : title.trim());

        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(raw.getBytes(StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder(hash.length * 2);
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) {
            return Integer.toHexString(raw.hashCode());
        }
    }
}
