package com.newswire.service;

import com.newswire.dto.NewsItem;
import com.newswire.source.FeedProperties;
import com.newswire.source.FeedSource;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;
import java.util.*;

@Service
public class RssNewsService {

    private final FeedProperties feedProperties;

    public RssNewsService(FeedProperties feedProperties) {
        this.feedProperties = feedProperties;
    }

    public List<NewsItem> fetchLatest(int maxItems) {
        List<NewsItem> all = new ArrayList<>();

        for (FeedSource feed : feedProperties.feeds()) {
            try {
                SyndFeed syndFeed = new SyndFeedInput().build(new XmlReader(new URL(feed.url())));
                if (syndFeed.getEntries() == null) continue;

                for (SyndEntry e : syndFeed.getEntries()) {
                    String title = safe(e.getTitle());
                    String link = safe(e.getLink());
                    if (title.isBlank() || link.isBlank()) continue;

                    Instant published = publishedInstant(e);

                    all.add(new NewsItem(
                            title,
                            link,
                            feed.sourceName(),
                            feed.category(),
                            published
                    ));
                }
            } catch (Exception ex) {
                System.err.println("RSS fetch failed: " + feed.url() + " :: " + ex.getMessage());
            }
        }

        // Dedupe by (normalized title + link)
        Map<String, NewsItem> deduped = new LinkedHashMap<>();
        for (NewsItem x : all) {
            String key = (x.title().toLowerCase(Locale.ROOT).trim() + "||" + x.url().trim());
            deduped.putIfAbsent(key, x);
        }

        return deduped.values().stream()
                .sorted(Comparator.comparing(NewsItem::publishedAt).reversed())
                .limit(Math.max(1, maxItems))
                .toList();
    }

    private Instant publishedInstant(SyndEntry e) {
        Date d = e.getPublishedDate();
        if (d == null) d = e.getUpdatedDate();
        return (d != null) ? d.toInstant() : Instant.now();
    }

    private String safe(String s) {
        return (s == null) ? "" : s;
    }
}
