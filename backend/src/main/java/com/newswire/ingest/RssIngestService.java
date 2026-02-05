package com.newswire.ingest;

import com.newswire.article.Article;
import com.newswire.util.FingerprintUtil;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class RssIngestService {

    public List<Article> ingest(String feedUrl, String sourceName, String category) {
        try {
            SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(feedUrl)));

            List<Article> articles = new ArrayList<>();
            for (SyndEntry entry : feed.getEntries()) {
                String title = safe(entry.getTitle());
                String url = safe(entry.getLink());
                if (title.isBlank() || url.isBlank()) continue;

                String summary = entry.getDescription() != null ? safe(entry.getDescription().getValue()) : "";
                Instant publishedAt = entry.getPublishedDate() != null
                        ? entry.getPublishedDate().toInstant()
                        : Instant.now();

                String fingerprint = FingerprintUtil.sha256(url.toLowerCase().trim() + "|" + title.toLowerCase().trim());

                articles.add(new Article(
                        title,
                        url,
                        summary,
                        sourceName,
                        category,
                        publishedAt,
                        fingerprint
                ));
            }
            return articles;

        } catch (Exception e) {
            throw new RuntimeException("Failed to ingest RSS feed: " + feedUrl, e);
        }
    }

    private static String safe(String s) {
        return s == null ? "" : s.trim();
    }
}

