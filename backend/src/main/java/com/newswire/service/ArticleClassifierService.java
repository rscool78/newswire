package com.newswire.service;

import com.newswire.article.Category;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Locale;
import java.util.Map;

/**
 * Classifies news articles by examining their title and summary.
 *
 * This first version uses deterministic keyword scoring.
 * Each supported category has its own scoring method so the
 * classification rules remain readable and easy to extend.
 */
@Service
public class ArticleClassifierService {

    /*
     * Minimum score required before the classifier replaces
     * the RSS feed's configured fallback category.
     */
    private static final int MINIMUM_SCORE = 2;

    /**
     * Classifies an article into the highest-scoring category.
     *
     * Every application category is included in the score map.
     * Categories whose scoring rules have not yet been implemented
     * return zero and therefore cannot win classification.
     *
     * When no category reaches the minimum score, or when multiple
     * categories tie for the highest score, the fallback category
     * is preserved.
     *
     * @param title article title
     * @param summary article summary or description
     * @param fallbackCategory category configured for the RSS feed
     * @return detected category or the configured fallback category
     */
    public Category classifyCategory(
        String title,
        String summary,
        Category fallbackCategory
    ) {
        Category safeFallback =
            fallbackCategory == null
                ? Category.GENERAL
                : fallbackCategory;

        /*
         * Combine and normalize the article title and summary
         * so each scoring method examines the same text.
         */
        String text = normalize(
            safeValue(title) + " " + safeValue(summary)
        );

        /*
         * EnumMap is designed specifically for enum keys.
         *
         * Every category is mapped now so new scoring methods
         * can be implemented without redesigning this method.
         */
        Map<Category, Integer> scores =
            new EnumMap<>(Category.class);

        scores.put(
            Category.GENERAL,
            scoreGeneral(text)
        );

        scores.put(
            Category.FINANCE,
            scoreFinance(text)
        );

        scores.put(
            Category.POLITICS,
            scorePolitics(text)
        );

        scores.put(
            Category.HEALTHCARE,
            scoreHealthcare(text)
        );

        scores.put(
            Category.TECHNOLOGY,
            scoreTechnology(text)
        );

        scores.put(
            Category.MILITARY,
            scoreMilitary(text)
        );

        scores.put(
            Category.MILITARY_INTELLIGENCE,
            scoreMilitaryIntelligence(text)
        );

        scores.put(
            Category.WORLD_POPULATION,
            scoreWorldPopulation(text)
        );

        scores.put(
            Category.WORLD_ECONOMIES,
            scoreWorldEconomies(text)
        );

        Category bestCategory = safeFallback;
        int bestScore = 0;
        boolean tied = false;

        /*
         * Find the category with the highest score.
         */
        for (
            Map.Entry<Category, Integer> entry
                : scores.entrySet()
        ) {
            int currentScore = entry.getValue();

            if (currentScore > bestScore) {
                bestCategory = entry.getKey();
                bestScore = currentScore;
                tied = false;
            } else if (
                currentScore == bestScore &&
                currentScore > 0
            ) {
                /*
                 * Two categories have equally strong evidence.
                 * Preserve the fallback rather than guessing.
                 */
                tied = true;
            }
        }

        /*
         * Do not override the feed category unless:
         * - the strongest score reaches the threshold
         * - only one category has that strongest score
         */
        if (bestScore < MINIMUM_SCORE || tied) {
            return safeFallback;
        }

        return bestCategory;
    }

    /**
     * GENERAL is currently used as a fallback category rather
     * than a keyword-driven classification result.
     */
    private int scoreGeneral(String text) {
        return 0;
    }

    /**
     * Finance scoring rules will be added in a later step.
     */
    private int scoreFinance(String text) {
        return 0;
    }

    /**
     * Calculates the politics score for an article.
     *
     * Specific political institutions and events receive
     * stronger weights than broad governmental terms.
     */
    private int scorePolitics(String text) {
        int score = 0;

        if (contains(text, "white house")) {
            score += 4;
        }

        if (contains(text, "presidential election")) {
            score += 4;
        }

        if (contains(text, "prime minister")) {
            score += 3;
        }

        if (contains(text, "congress")) {
            score += 3;
        }

        if (contains(text, "parliament")) {
            score += 3;
        }

        if (contains(text, "senate")) {
            score += 2;
        }

        if (contains(text, "election")) {
            score += 2;
        }

        if (contains(text, "campaign")) {
            score += 2;
        }

        if (contains(text, "legislation")) {
            score += 2;
        }

        if (contains(text, "government")) {
            score += 1;
        }

        if (contains(text, "president")) {
            score += 1;
        }

        return score;
    }

    /**
     * Healthcare scoring rules will be added in a later step.
     */
    private int scoreHealthcare(String text) {
        return 0;
    }

    /**
     * Calculates the technology score for an article.
     *
     * Specific technical phrases receive larger weights than
     * broad words such as "software" or "computer."
     */
    private int scoreTechnology(String text) {
        int score = 0;

        if (contains(text, "artificial intelligence")) {
            score += 4;
        }

        if (contains(text, "machine learning")) {
            score += 4;
        }

        if (contains(text, "cybersecurity")) {
            score += 3;
        }

        if (contains(text, "cyber security")) {
            score += 3;
        }

        if (contains(text, "data breach")) {
            score += 3;
        }

        if (contains(text, "cloud computing")) {
            score += 3;
        }

        if (contains(text, "semiconductor")) {
            score += 2;
        }

        if (contains(text, "software")) {
            score += 2;
        }

        if (contains(text, "technology")) {
            score += 2;
        }

        if (contains(text, "computer")) {
            score += 1;
        }

        if (contains(text, "ai")) {
            score += 2;
        }

        return score;
    }

    /**
     * Military scoring rules will be added in a later step.
     */
    private int scoreMilitary(String text) {
        return 0;
    }

    /**
     * Military-intelligence scoring rules will be added later.
     */
    private int scoreMilitaryIntelligence(
        String text
    ) {
        return 0;
    }

    /**
     * World-population scoring rules will be added later.
     */
    private int scoreWorldPopulation(String text) {
        return 0;
    }

    /**
     * World-economies scoring rules will be added later.
     */
    private int scoreWorldEconomies(String text) {
        return 0;
    }

    /**
     * Checks whether normalized article text contains a
     * complete word or phrase.
     *
     * Padding both values with spaces prevents short keywords
     * from matching inside unrelated words. For example,
     * "ai" will not match the word "said."
     */
    private boolean contains(
        String text,
        String phrase
    ) {
        String paddedText =
            " " + normalize(text) + " ";

        String paddedPhrase =
            " " + normalize(phrase) + " ";

        return paddedText.contains(paddedPhrase);
    }

    /**
     * Normalizes text before classification.
     *
     * The method:
     * - converts text to lowercase
     * - replaces punctuation with spaces
     * - collapses repeated whitespace
     * - removes leading and trailing whitespace
     */
    private String normalize(String value) {
        return safeValue(value)
            .toLowerCase(Locale.ROOT)
            .replaceAll("[^a-z0-9]+", " ")
            .replaceAll("\\s+", " ")
            .trim();
    }

    /**
     * Converts null values into empty strings so incomplete
     * RSS entries can be processed safely.
     */
    private String safeValue(String value) {
        return value == null ? "" : value;
    }
}