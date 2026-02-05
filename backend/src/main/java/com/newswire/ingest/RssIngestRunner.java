package com.newswire.ingest;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("ingest")
public class RssIngestRunner implements CommandLineRunner {

    private final RssIngestService rssIngestService;

    public RssIngestRunner(RssIngestService rssIngestService) {
        this.rssIngestService = rssIngestService;
    }

    @Override
    public void run(String... args) {
        System.out.println("Spring Boot RSS ingestion started");

        rssIngestService.ingest("https://feeds.bbci.co.uk/news/rss.xml");

        System.out.println("Spring Boot RSS ingestion finished");
    }
}

