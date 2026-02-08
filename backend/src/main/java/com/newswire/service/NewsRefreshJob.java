package com.newswire.service;

import com.newswire.config.RefreshProperties;
import com.newswire.dto.NewsItem;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NewsRefreshJob {

    private final RssNewsService rss;
    private final ArticleStoreService store;
    private final RefreshStatus status;
    private final RefreshProperties props;

    public NewsRefreshJob(RssNewsService rss, ArticleStoreService store, RefreshStatus status, RefreshProperties props) {
        this.rss = rss;
        this.store = store;
        this.status = status;
        this.props = props;
    }

    @Scheduled(
            fixedDelayString = "${newswire.refresh.fixedDelayMs:600000}",
            initialDelayString = "${newswire.refresh.initialDelayMs:10000}"
    )
    public void refresh() {
        if (!props.enabled()) return;

        status.markRun();
        try {
            List<NewsItem> latest = rss.fetchLatest(props.fetchLimit());
            int inserted = store.saveIfNew(latest);
            status.markSuccess();

            System.out.println("[scheduler] RSS refresh — inserted " + inserted);
        } catch (Exception e) {
            status.markError(e.getClass().getSimpleName() + ": " + e.getMessage());
            e.printStackTrace(); // dev-friendly; later we’ll switch to a logger

        }        
        
    }
}

