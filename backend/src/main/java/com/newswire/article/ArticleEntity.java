package com.newswire.article;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "articles",
        indexes = {
                @Index(name = "idx_articles_fingerprint", columnList = "fingerprint", unique = true),
                @Index(name = "idx_articles_category_published", columnList = "category,publishedAt")
        }
)
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 512)
    private String title;

    @Column(nullable = false, length = 2048)
    private String url;

    @Column(columnDefinition = "text")
    private String summary;

    @Column(nullable = false, length = 128)
    private String sourceName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 64)
    private Category category;

    @Column(nullable = false)
    private Instant publishedAt;

    @Column(nullable = false, unique = true, length = 64)
    private String fingerprint;

    protected ArticleEntity() { }

    public ArticleEntity(String title, String url, String summary, String sourceName,
                         Category category, Instant publishedAt, String fingerprint) {
        this.title = title;
        this.url = url;
        this.summary = summary;
        this.sourceName = sourceName;
        this.category = category;
        this.publishedAt = publishedAt;
        this.fingerprint = fingerprint;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getUrl() { return url; }
    public String getSummary() { return summary; }
    public String getSourceName() { return sourceName; }
    public Category getCategory() { return category; }
    public Instant getPublishedAt() { return publishedAt; }
    public String getFingerprint() { return fingerprint; }
}
