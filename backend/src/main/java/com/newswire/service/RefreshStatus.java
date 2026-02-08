package com.newswire.service;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class RefreshStatus {

    private final AtomicReference<Instant> lastRun = new AtomicReference<>();
    private final AtomicReference<Instant> lastSuccess = new AtomicReference<>();
    private final AtomicReference<String> lastError = new AtomicReference<>();

    public void markRun() {
        lastRun.set(Instant.now());
    }

    public void markSuccess() {
        lastSuccess.set(Instant.now());
        lastError.set(null);
    }

    public void markError(String msg) {
        lastError.set(msg);
    }

    public Instant getLastRun() {
        return lastRun.get();
    }

    public Instant getLastSuccess() {
        return lastSuccess.get();
    }

    public String getLastError() {
        return lastError.get();
    }
}
