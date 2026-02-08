package com.newswire.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "newswire.refresh")
public record RefreshProperties(
        boolean enabled,
        long fixedDelayMs,
        long initialDelayMs,
        int fetchLimit
) {}
