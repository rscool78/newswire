package com.newswire.service;

import org.springframework.stereotype.Service;

@Service
public class SummarizationService {

    public String summarize(String title, String summary) {
        if (summary != null && !summary.isBlank()) {
            String trimmed = summary.trim();
            return trimmed.length() > 240 ? trimmed.substring(0, 240) + "…" : trimmed;
        }

        if (title != null && !title.isBlank()) {
            return title;
        }

        return "";
    }
}