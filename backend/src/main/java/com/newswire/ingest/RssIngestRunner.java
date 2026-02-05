package com.newswire.ingest;

import com.newswire.article.Article;
import com.newswire.article.Category;
import com.newswire.source.FeedProperties;
import com.newswire.source.FeedSource;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;


@Component
@Profile("ingest")
public class RssIngestRunner implements CommandLineRunner {

    private final RssIngestService rss;
    private final FeedProperties feedProps;

    public RssIngestRunner(RssIngestService rss, FeedProperties feedProps) {
        this.rss = rss;
        this.feedProps = feedProps;
    }

    @Override
    public void run(String... args) {
        System.out.println("Spring Boot RSS ingestion started");

       
        List<FeedSource> feeds = feedProps.feeds();
        if (feeds == null || feeds.isEmpty()) {
            System.out.println("No feeds configured under newswire.feeds");
            return;
        }

         List<Article> all = new ArrayList<>();
        for (FeedSource feed : feeds) {
            System.out.println("Ingesting: " + feed.sourceName() + " [" + feed.category() + "] " + feed.url());
            all.addAll(rss.ingest(feed.url(), feed.sourceName(), feed.category()));
        }

        System.out.println("Total normalized articles: " + all.size());

        // 1) Dedupe by fingerprint (keep newest) + sort newest first
        List<Article> dedupedSorted = all.stream()
                .collect(Collectors.toMap(
                        Article::fingerprint,
                        a -> a,
                        (a, b) -> a.publishedAt().isAfter(b.publishedAt()) ? a : b,
                        LinkedHashMap::new
                ))
                .values()
                .stream()
                .sorted(Comparator.comparing(Article::publishedAt).reversed())
                .toList();

        System.out.println("After dedupe: " + dedupedSorted.size());

        // 2) Group by category and print top 5 per category
        //Map<com.newswire.article.Category, List<Article>> byCategory =
        //No need to reference com.newswire.article.Category fully qualified just import it once
        Map<Category, List<Article>> byCategory =
            //all.stream().limit(8).forEach(a ->
            //all.stream().collect(Collectors.groupingBy(Article::category));
            dedupedSorted.stream().collect(Collectors.groupingBy(Article::category));

       
        //byCategory.forEach((cat, list) ->
        //Replacing grouping map with a TreeMap or sort keys
        byCategory.entrySet().stream()
            .sorted(Map.Entry.comparingByKey())
            .forEach(entry -> {
                Category cat = entry.getKey(); //the category
                List<Article> list = entry.getValue(); //the list of articles

            //System.out.println(" - [" + a.category() + "] " + a.title())
            System.out.println("\nCategory: " + cat + "(" + list.size() + ")");
            list.stream().limit(5).forEach(a -> System.out.println(" - " + a.title()));

        });
                
        System.out.println("Spring Boot RSS ingestion finished");
    }
}



