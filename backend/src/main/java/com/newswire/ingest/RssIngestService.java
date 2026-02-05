package com.newswire.ingest;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public class RssIngestService {

    public void ingest(String feedUrl) {
        try {
            URL url = new URL(feedUrl);
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(url));

            System.out.println("Feed title: " + feed.getTitle());

            for (SyndEntry entry : feed.getEntries()) {
                System.out.println(" - " + entry.getTitle());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to ingest RSS feed: " + feedUrl, e);
        }
    }
}
