package com.newswire.source;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "newswire")
public record FeedProperties(
        List<FeedSource> feeds
) {}
