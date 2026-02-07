/*package com.newswire.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class NewsController {

    @GetMapping("/api/news")
    public List<Map<String, String>> news() {
        return List.of(
                Map.of("title", "Spring Boot backend is live", "url", "https://example.com"),
                Map.of("title", "React frontend connected", "url", "https://vitejs.dev")
        );
    }
}*/

package com.newswire.controller;

import com.newswire.dto.NewsItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class NewsController {
    
    @GetMapping("/api/news")
    public List<NewsItem> news() {
        return List.of(
            new NewsItem("Spring Boot backend is live", "https://example.com"),
            new NewsItem("React frontend connected", "https://vitejs.dev")
        );
    }
}

