package com.newswire.article;

public enum Category {
    FINANCE,
    POLITICS,
    HEALTHCARE,
    TECHNOLOGY,
    MILITARY,
    MILITARY_INTELLIGENCE,
    WORLD_POPULATION,
    WORLD_ECONOMIES;

    public static Category from(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Category value is blank");
        }
        return Category.valueOf(value.trim().toUpperCase().replace(' ', '_'));
    }
}
