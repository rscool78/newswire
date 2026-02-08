package com.newswire.legacy;

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
import java.util.stream.Collectors;

//@Service
@Deprecated
public class RssNewsService {

    private final FeedProperties feedProperties;

    public RssNewsService(FeedProperties feedProperties) {
        this.feedProperties = feedProperties;
    }

    public List<NewsItem> fetchLatest(int maxItems) {
        List<EntryWithMeta> all = new ArrayList<>();

        for (FeedSource feed : feedProperties.feeds()) {
            try {
                SyndFeed syndFeed = new SyndFeedInput().build(new XmlReader(new URL(feed.url())));
                if (syndFeed.getEntries() == null) continue;

                for (SyndEntry e : syndFeed.getEntries()) {
                    String title = safe(e.getTitle());
                    String link = safe(e.getLink());
                    //if (title.isBlank() && link.isBlank()) continue;
                    if (title.isBlank() || link.isBlank()) continue;

                    Instant published = publishedInstant(e);

                    //all.add(new EntryWithMeta(
                    all.add(new NewsItem(
                            title,
                            link,
                            //publishedInstant(e),
                            feed.sourceName(),
                            feed.category(),
                            //String.valueOf(feed.category())
                            published
                    ));
                }
            } catch (Exception ex) {
                System.err.println("RSS fetch failed: " + feed.url());
            }
        }

        // Dedupe by (normalized title + link)
        //Map<String, EntryWithMeta> deduped = new LinkedHashMap<>();
        Map<String, NewsItem> deduped = new LinkedHashMap<>();
        //for (EntryWithMeta x : all) {
        for (NewsItem x : all) {
            String key = (x.title.toLowerCase(Locale.ROOT).trim() + "||" + x.url.trim());
            deduped.putIfAbsent(key, x);
        }

        return deduped.values().stream()
                //.sorted(Comparator.comparing((EntryWithMeta x) -> x.published).reversed())
                .sorted(Comparator.comparing(NewsItem::publishedAt).reversed())
                .limit(Math.max(1, maxItems))
                //.map(x -> new NewsItem(x.title, x.url))
                //.collect(Collectors.toList());
                .toList();
    }

    private Instant publishedInstant(SyndEntry e) {
        Date d = e.getPublishedDate();
        if (d == null) d = e.getUpdatedDate();
        // Final fallback: NOW (not EPOCH)
        return (d != null) ? d.toInstant() : Instant.now();
        //return (d != null) ? d.toInstant() : Instant.EPOCH;  Don't use Instant.EPOCH for UI-facing timestamps
    }

    private String safe(String s) {
        return (s == null) ? "" : s;
    }

    private static class EntryWithMeta {
        final String title;
        final String url;
        final Instant published;
        final String sourceName;
        final String category;

        EntryWithMeta(String title, String url, Instant published, String sourceName, String category) {
            this.title = title;
            this.url = url;
            this.published = published;
            this.sourceName = sourceName;
            this.category = category;
        }
    }
}



/*package com.newswire.service;

import com.newswire.config.NewswireProperties;
import com.newswire.dto.NewsItem;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RssNewsService {

    private final NewswireProperties props;

    public RssNewsService(NewswireProperties props) {
        this.props = props;
    }

    public List<NewsItem> fetchLatest() {
        List<SyndEntry> allEntries = new ArrayList<>();

        for (String feedUrl : props.getFeeds()) {
            try {
                SyndFeed feed = new SyndFeedInput().build(new XmlReader(new URL(feedUrl)));
                if (feed.getEntries() != null) {
                    allEntries.addAll(feed.getEntries());
                }
            } catch (Exception ex) {
                // Keep going if one feed fails
                System.err.println("RSS fetch failed: " + feedUrl + " :: " + ex.getMessage());
            }
        }

        // Dedupe by normalized title+link
        Map<String, SyndEntry> deduped = new LinkedHashMap<>();
        for (SyndEntry e : allEntries) {
            String title = safe(e.getTitle());
            String link = safe(e.getLink());
            if (title.isBlank() && link.isBlank()) continue;

            String key = (title.toLowerCase(Locale.ROOT).trim() + "||" + link.trim());
            deduped.putIfAbsent(key, e);
        }

        // Sort by published date desc (fallback to "now" if missing)
        return deduped.values().stream()
                .sorted(Comparator.comparing(this::publishedInstant).reversed())
                .limit(Math.max(1, props.getMaxItems()))
                .map(e -> new NewsItem(safe(e.getTitle()), safe(e.getLink())))
                .collect(Collectors.toList());
    }

    private Instant publishedInstant(SyndEntry e) {
        Date d = e.getPublishedDate();
        if (d == null) d = e.getUpdatedDate();
        return (d != null) ? d.toInstant() : Instant.EPOCH;
    }

    private String safe(String s) {
        return (s == null) ? "" : s;
    }
}*/
