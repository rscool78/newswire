package com.newswire.ingest;

import com.newswire.article.Article;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("ingest")
public class RssIngestRunner implements CommandLineRunner {

    private final RssIngestService rss;

    public RssIngestRunner(RssIngestService rss) {
        this.rss = rss;
    }

    @Override
    public void run(String... args) {
        System.out.println("Spring Boot RSS ingestion started");

        List<Article> articles = rss.ingest(
                "https://feeds.bbci.co.uk/news/rss.xml",
                "BBC News",
                "world"
        );

        System.out.println("Normalized articles: " + articles.size());
        articles.stream().limit(5).forEach(a ->
                System.out.println(" - [" + a.sourceName() + "] " + a.title() + " (" + a.url() + ")")
        );

        System.out.println("Spring Boot RSS ingestion finished");
    }
}


