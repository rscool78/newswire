package com.newswire.dto;

public record PageMeta(
        int number,
        int size,
        long totalElements,
        int totalPages
) {}