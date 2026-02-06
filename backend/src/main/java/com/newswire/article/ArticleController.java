package com.newswire.article;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller:
 * - Handles HTTP requests
 * - Reads params (page/size)
 * - Delegates to ArticleService
 * - Returns JSON
 *
 * Controller stays "thin": no repository calls, no DB logic.
 */
@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService service;

    public ArticleController(ArticleService service) {
        this.service = service;
    }

    /**
     * GET /api/articles?page=0&size=20
     * Returns the newest articles (paged) across all categories.
     */
    @GetMapping
    public Page<ArticleDto> getLatest(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return service.getLatest(pageable);
    }

    /**
     * GET /api/articles/category/TECHNOLOGY?page=0&size=20
     * Returns newest articles for a category (paged).
     *
     * Spring converts "TECHNOLOGY" -> Category.TECHNOLOGY automatically.
     */
    @GetMapping("/category/{category}")
    public Page<ArticleDto> getByCategory(
            @PathVariable Category category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return service.getByCategory(category, pageable);
    }
}

