package com.newswire.article;

/*
 * Spring Data JPA repository.
 *
 * This interface is the ONLY place where we talk directly to the database.
 * Spring will generate the implementation for us at runtime.
 */

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


/*
 * ArticleRepository manages persistence for ArticleEntity.
 *
 * <ArticleEntity, Long>
 *   - ArticleEntity = JPA @Entity mapped to the "articles" table
 *   - Long = type of the primary key (id)
 *
 * This repository supports BOTH:
 *   1) write-path operations (ingestion, deduplication)
 *   2) read-path operations (API queries)
 */

//public interface ArticleRepository extends JpaRepository<Article, Long> { 
public interface ArticleRepository extends JpaRepository<ArticleEntity, Long> {       
    
    // --------------------------------------------------
    // WRITE-PATH (INGESTION)
    // --------------------------------------------------

    /*
     * Checks if an article already exists based on its fingerprint.
     *
     * Why:
     * - Fingerprint is a SHA-256 hash of (url + title)
     * - This prevents duplicate articles across RSS feeds
     * - Used BEFORE saving during ingestion
     *
     * Spring automatically generates SQL like:
     *   SELECT EXISTS (
     *     SELECT 1 FROM articles WHERE fingerprint = ?
     *   )
     *
     * Backed by a UNIQUE INDEX → fast and safe under concurrency.
     */
    boolean existsByFingerprint(String fingerprint);


    // --------------------------------------------------
    // READ-PATH (API)
    // --------------------------------------------------

    /*
     * Fetches ALL articles ordered by published date (newest first).
     *
     * Pageable:
     * - page number
     * - page size
     * - optional sorting (though we hardcode sorting here)
     *
     * Returns Page<ArticleEntity>, which contains:
     * - content (list of articles)
     * - total pages
     * - total elements
     * - current page info
     */
    Page<ArticleEntity> findAllByOrderByPublishedAtDesc(Pageable pageable);

    /*
     * Fetches articles for ONE category, ordered newest → oldest.
     *
     * Used by API endpoints like:
     *   /api/articles?category=TECHNOLOGY
     *
     * Spring generates SQL similar to:
     *   SELECT * FROM articles
     *   WHERE category = ?
     *   ORDER BY published_at DESC
     *   LIMIT ? OFFSET ?
     */
    Page<ArticleEntity> findByCategoryOrderByPublishedAtDesc(
            Category category,
            Pageable pageable
    );
}