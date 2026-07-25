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

        scores.put(
            Category.SPORTS, 
            scoreSports(text)
        );

        scores.put(
            Category.ENTERTAINMENT, 
            scoreEntertainment(text));

        scores.put(
            Category.CULTURE, 
            scoreCulture(text));

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
        int score = 0;

        if (contains(text, "federal reserve")) {
            score += 4;
        }

        if (contains(text, "finance")) {
            score += 3;
        }

        if (contains(text, "stock market")) {
            score += 4;
        }

        if (contains(text, "dow jones")) {
            score += 4;
        }

        if (contains(text, "nasdaq")) {
            score += 4;
        }

        if (contains(text, "s p 500")) {
            score += 4;
        }

        if (contains(text, "credit rating")) {
            score += 3;
        }

        if (contains(text, "inflation")) {
            score += 3;
        }

        if (contains(text, "interest rates")) {
            score += 3;
        }

        if (contains(text, "earnings")) {
            score += 3;
        }

        if (contains(text, "gdp")) {
            score += 3;
        }

        if (contains(text, "recession")) {
            score += 3;
        }

        if (contains(text, "unemployment")) {
            score += 2;
        }

        if (contains(text, "economy")) {
            score += 2;
        }

        if (contains(text, "stocks")) {
            score += 2;
        }

        if (contains(text, "bonds")) {
            score += 2;
        }

        if (contains(text, "investment")) {
            score += 2;
        }

        if (contains(text, "investors")) {
            score += 2;
        }

        if (contains(text, "market")) {
            score += 1;
        }

        if (contains(text, "financial")) {
            score += 3;
        }

        if (contains(text, "revenue")) {
            score += 3;
        }

        if (contains(text, "expenses")) {
            score += 3;
        }

        return score;
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
     * Calculates the healthcare score for an article.
     *
     * Highly specific medical organizations and public health
     * terms receive stronger weights than general healthcare
     * terminology.
     */
    private int scoreHealthcare(String text) {
        int score = 0;

        if (contains(text, "centers for disease control")) {
            score += 4;
        }

        if (contains(text, "food and drug administration")) {
            score += 4;
        }

        if (contains(text, "public health")) {
            score += 4;
        }

        if (contains(text, "clinical trial")) {
            score += 4;
        }

        if (contains(text, "medical research")) {
            score += 4;
        }

        if (contains(text, "cdc")) {
            score += 3;
        }

        if (contains(text, "fda")) {
            score += 3;
        }

        if (contains(text, "vaccine")) {
            score += 4;
        }

        if (contains(text, "vaccination")) {
            score += 3;
        }

        if (contains(text, "hospital")) {
            score += 1;
        }

        if (contains(text, "physician")) {
            score += 5;
        }

        if (contains(text, "doctor")) {
            score += 2;
        }

        if (contains(text, "patient")) {
            score += 1;
        }

        if (contains(text, "disease")) {
            score += 5;
        }

        if (contains(text, "cancer")) {
            score += 5;
        }

        if (contains(text, "virus")) {
            score += 2;
        }

        if (contains(text, "infection")) {
            score += 2;
        }

        if (contains(text, "medicine")) {
            score += 2;
        }

        if (contains(text, "medical")) {
            score += 2;
        }

        if (contains(text, "healthcare")) {
            score += 4;
        }

        if (contains(text, "health care")) {
            score += 2;
        }

        if (contains(text, "health")) {
            score += 3;
        }

        return score;
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
            score += 5;
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
            score += 5;
        }

        if (contains(text, "OpenAI")) {
            score += 4;
        }
        
        if (contains(text, "Anthropic")) {
            score += 4;
        }

        if (contains(text, "LLM")) {
            score += 4;
        }

        if (contains(text, "foundation model")) {
            score += 4;
        }
        
        
        

        return score;
    }

    /**
     * Calculates the military score for an article.
     *
     * Specific doctrine, operations, weapons systems, military
     * organizations, and force-structure terms receive stronger
     * weights than broad military terminology.
     */
    private int scoreMilitary(String text) {
        int score = 0;

        // Major military concepts
        if (contains(text, "asymmetric warfare")) {
            score += 4;
        }

        if (contains(text, "nuclear weapons")) {
            score += 4;
        }

        if (contains(text, "war games")) {
            score += 4;
        }

        if (contains(text, "defcon")) {
            score += 4;
        }

        if (contains(text, "demilitarized zone")) {
            score += 4;
        }

        if (contains(text, "rules of engagement")) {
            score += 4;
        }

        if (contains(text, "joint task force")) {
            score += 4;
        }

        if (contains(text, "special operations")) {
            score += 4;
        }

        if (contains(text, "carrier strike group")) {
            score += 4;
        }

        if (contains(text, "amphibious assault")) {
            score += 4;
        }

        // Equipment / platforms
        if (contains(text, "battle group")) {
            score += 3;
        }

        if (contains(text, "submarine")) {
            score += 3;
        }

        if (contains(text, "unmanned aerial vehicle")) {
            score += 4;
        }

        else if (contains(text, "uav")) {
            score += 3;
        }

        else if (contains(text, "drone")) {
            score +=3;
        }

        if (contains(text, "aircraft carrier")) {
            score += 3;
        }

        if (contains(text, "destroyer")) {
            score += 3;
        }

        if (contains(text, "frigate")) {
            score += 3;
        }

        if (contains(text, "battleship")) {
            score += 3;
        }

        if (contains(text, "tank")) {
            score += 3;
        }
        
        if (contains(text, "armored vehicle")) {score += 3;}
        
        if (contains(text, "fighter jet")) {score += 3;}
        
        if (contains(text, "fighter aircraft")) {score += 3;}
        
        if (contains(text, "bomber")) {score += 3;}
        
        if (contains(text, "ballistic missile")) {score += 4;}

        else if (contains(text, "cruise missile")) {score += 4;}
        
        else if (contains(text, "missile")) {score += 1;}

        if (contains(text, "artillery")) {score += 3;}
        
        if (contains(text, "howitzer")) {score += 3;}
        
        

        // Military organizations
        if (contains(text, "army")) {score += 2;}
        if (contains(text, "navy")) {score += 2;}
        if (contains(text, "air force")) {score += 2;}
        if (contains(text, "marine corps")) {score += 2;}
        if (contains(text, "marines")) {score += 2;}
        if (contains(text, "coast guard")) {score += 2;}
        if (contains(text, "national guard")) {score += 2;}
        if (contains(text, "space force")) {score += 2;}
        if (contains(text, "Defense Department")) {score += 5;}
        if (contains(text, "War Department")) {score += 5;}
        if (contains(text, "Pentagon")) {score += 5;}

        // Personnel
        if (contains(text, "soldier")) {score += 2;}
        if (contains(text, "troops")) {score += 5;}
        if (contains(text, "infantry")) {score += 2;}
        if (contains(text, "battalion")) {score += 2;}
        if (contains(text, "brigade")) {score += 2;}
        if (contains(text, "military division")) {score += 2;}
        if (contains(text, "platoon")) {score += 2;}
        if (contains(text, "squadron")) {score += 2;}
        if (contains(text, "troop deployment")) {score += 4;}
        if (contains(text, "mobilization")) {score += 2;}
        if (contains(text, "military exercise")) {score += 2;}

        // Defense industry / procurement
        if (contains(text, "defense contractor")) {score += 2;}
        if (contains(text, "defense procurement")) {score += 2;}
        if (contains(text, "private military company")) {score += 2;}
        if (contains(text, "mercenary")) {score += 2;}
        if (contains(text, "weapons of mass destruction")) {score += 3;}

        // General military terms
        if (contains(text, "military base")) {score += 1;}
        if (contains(text, "national defense")) {score += 1;}
        if (contains(text, "combat")) {score += 5;}
        if (contains(text, "battlefield")) {score += 1;}
        if (contains(text, "garrison")) {score += 1;}
        
        return score;
    }

    /**
     * Calculates the military intelligence score for an article.
     *
     * Intelligence agencies, intelligence disciplines, surveillance,
     * reconnaissance, espionage, and classified operations receive
     * stronger weights than broad intelligence terminology.
     */
    private int scoreMilitaryIntelligence(String text) {
        int score = 0;

        // Intelligence disciplines
        if (contains(text, "signals intelligence")) {score += 4;}
        if (contains(text, "human intelligence")) {score += 4;}
        if (contains(text, "geospatial intelligence")) {score += 4;}
        if (contains(text, "imagery intelligence")) {score += 4;}
        if (contains(text, "measurement and signature intelligence")) {score += 4;}
        if (contains(text, "open source intelligence")) {score += 4;}
        if (contains(text, "all source intelligence")) {score += 4;}
        if (contains(text, "battlefield intelligence")) {score += 4;}
        if (contains(text, "recon aircraft")) {score += 3;}
        if (contains(text, "surveillance aircraft")) {score += 3;}
        if (contains(text, "signals intercept")) {score += 3;}
        if (contains(text, "electronic intelligence")) {score += 4;}

        // Intelligence abbreviations
        if (contains(text, "sigint")) {score += 3;}
        if (contains(text, "humint")) {score += 3;}
        if (contains(text, "geoint")) {score += 3;}
        if (contains(text, "imint")) {score += 3;}
        if (contains(text, "masint")) {score += 3;}
        if (contains(text, "osint")) {score += 3;}

        // Intelligence organizations
        if (contains(text, "national security agency")) {score += 4;}
        if (contains(text, "central intelligence agency")) {score += 4;}
        if (contains(text, "defense intelligence agency")) {score += 4;}
        if (contains(text, "national reconnaissance office")) {score += 4;}
        if (contains(text, "national geospatial intelligence agency")) {score += 4;}

        if (contains(text, "nsa")) {score += 3;}
        if (contains(text, "cia")) {score += 3;}
        if (contains(text, "dia")) {score += 3;}
        if (contains(text, "nro")) {score += 3;}
        if (contains(text, "nga")) {score += 3;}

        // Intelligence activities
        if (contains(text, "espionage")) {score += 3;}
        if (contains(text, "counterintelligence")) {score += 3;}
        if (contains(text, "covert operation")) {score += 3;}
        if (contains(text, "clandestine")) {score += 3;}
        if (contains(text, "spy satellite")) {score += 3;}
        if (contains(text, "satellite reconnaissance")) {score += 3;}
        else if (contains(text, "reconnaissance")) {score += 2;}
        else if (contains(text, "recon")) {score += 1;}
        if (contains(text, "electronic surveillance")) {score += 2;}
        else if (contains(text, "surveillance")) {score += 1;}
        if (contains(text, "signals intercept")) {score += 3;}
        else if (contains(text, "signal intercept")) {score += 2;}
        if (contains(text, "intercepted communications")) {score += 2;}
        if (contains(text, "classified documents")) {score += 3;}
        else if (contains(text, "classified")) {score += 2;}
        else if (contains(text, "declassified")) {score += 2;}
        if (contains(text, "secure communications")) {score += 2;}

        // Intelligence personnel
        if (contains(text, "intelligence officer")) {score += 2;}
        if (contains(text, "intelligence analyst")) {score += 2;}
        if (contains(text, "field agent")) {score += 2;}
        if (contains(text, "case officer")) {score += 2;}
        if (contains(text, "spy")) {score += 2;}
        if (contains(text, "informant")) {score += 2;}
        if (contains(text, "intelligence asset")) {score += 2;}
        if (contains(text, "double agent")) {score += 3;}

        // General terms
        
        if (contains(text, "intelligence surveillance reconnaissance")) {score += 4;} 
        else if (contains(text, "isr")) {score += 3;} 
        else if (contains(text, "intelligence")) {score += 1;}
        if (contains(text, "fusion center")) {score += 3;}
        if (contains(text, "intelligence briefing")) {score += 2;}
        if (contains(text, "targeting intelligence")) {score += 3;}
        if (contains(text, "intelligence report")) {score += 2;}
        if (contains(text, "collection management")) {score += 3;}
        

        // Cyber intelligence
        if (contains(text, "cyber intelligence")) {score += 3;}
        if (contains(text, "threat intelligence")) {score += 3;}
        if (contains(text, "cyber espionage")) {score += 4;}

        // Space intelligence
        if (contains(text, "overhead reconnaissance")) {score += 3;}
        if (contains(text, "electro optical")) {score += 3;}
        if (contains(text, "synthetic aperture radar")) {score += 3;}

        // Foreign intelligence services
        if (contains(text, "mi6")) {score += 3;}
        if (contains(text, "mi5")) {score += 3;}
        if (contains(text, "mossad")) {score += 3;}
        if (contains(text, "fsb")) {score += 3;}
        if (contains(text, "gru")) {score += 3;}
        if (contains(text, "government communications headquarters")) {score += 3;}
        if (contains(text, "asis")) {score += 3;}
        if (contains(text, "csis")) {score += 3;}
        if (contains(text, "raw")) {score += 3;}
        if (contains(text, "isi")) {score += 3;}
        if (contains(text, "ministry of state security")) {score += 3;}   // China's Ministry of State Security
        if (contains(text, "foreign intelligence service")) {score += 3;}

        return score;
    }

    /**
     * Calculates the world population score for an article.
     *
     * Population growth, demographics, migration, census data,
     * fertility, mortality, and urbanization receive stronger
     * weights than general population terminology.
     */
    private int scoreWorldPopulation(String text) {
        int score = 0;

        // Population studies
        if (contains(text, "population growth")) {
            score += 4;
        }

        if (contains(text, "population decline")) {
            score += 4;
        }

        if (contains(text, "population density")) {
            score += 4;
        }

        if (contains(text, "population projection")) {
            score += 4;
        }

        if (contains(text, "population forecast")) {
            score += 4;
        }

        if (contains(text, "world population")) {
            score += 4;
        }

        if (contains(text, "demographic transition")) {
            score += 4;
        }

        if (contains(text, "population census")) {
            score += 4;
        }

        if (contains(text, "census bureau")) {
            score += 4;
        }

        if (contains(text, "census data")) {
            score += 4;
        }

        // Demographics
        if (contains(text, "birth rate")) {
            score += 3;
        }

        if (contains(text, "death rate")) {
            score += 3;
        }

        if (contains(text, "fertility rate")) {
            score += 3;
        }

        if (contains(text, "life expectancy")) {
            score += 3;
        }

        if (contains(text, "median age")) {
            score += 3;
        }

        if (contains(text, "aging population")) {
            score += 3;
        }

        if (contains(text, "population pyramid")) {
            score += 3;
        }

        if (contains(text, "dependency ratio")) {
            score += 3;
        }

        if (contains(text, "demographic")) {
            score += 3;
        }

        // Migration
        if (contains(text, "immigration")) {
            score += 3;
        }

        if (contains(text, "emigration")) {
            score += 3;
        }

        if (contains(text, "migration patterns")) {
            score += 3;
        } else if (contains(text, "migration trends")) {
            score += 3;
        } else if (contains(text, "population migration")) {
            score += 3;
        } else if (contains(text, "migration")) {
            score += 2;
        }

        if (contains(text, "refugee population")) {
            score += 3;
        }

        if (contains(text, "displaced persons")) {
            score += 3;
        }

        // Urbanization
        if (contains(text, "urbanization")) {
            score += 3;
        }

        if (contains(text, "urban population")) {
            score += 3;
        }

        if (contains(text, "rural population")) {
            score += 3;
        }

        if (contains(text, "megacity")) {
            score += 3;
        }

        // International organizations
        if (contains(text, "un population division")) {
            score += 4;
        }

        if (contains(text, "un population fund")) {
            score += 4;
        }

        if (contains(text, "unfpa")) {
            score += 3;
        }

        // Statistics
        if (contains(text, "births")) {
            score += 2;
        }

        if (contains(text, "deaths")) {
            score += 2;
        }

        if (contains(text, "fertility")) {
            score += 2;
        }

        if (contains(text, "mortality")) {
            score += 2;
        }

        if (contains(text, "longevity")) {
            score += 2;
        }

        if (contains(text, "demographics")) {
            score += 2;
        }

        // Additional population terms
        if (contains(text, "replacement rate")) {
            score += 3;
        }

        if (contains(text, "replacement fertility")) {
            score += 3;
        }

        if (contains(text, "natural increase")) {
            score += 3;
        }

        if (contains(text, "population boom")) {
            score += 3;
        }

        if (contains(text, "population explosion")) {
            score += 3;
        }

        if (contains(text, "working age population")) {
            score += 3;
        }

        if (contains(text, "elderly population")) {
            score += 3;
        }

        if (contains(text, "child population")) {
            score += 3;
        }

        if (contains(text, "household survey")) {
            score += 2;
        }

        if (contains(text, "demographic survey")) {
            score += 3;
        }

        // General terms
        if (contains(text, "population")) {
            score += 1;
        }

        if (contains(text, "census")) {
            score += 1;
        }

        if (contains(text, "resident")) {
            score += 1;
        }

        return score;
    }

    /**
     * Calculates the world economies score for an article.
     *
     * Macroeconomic indicators, central banks, international trade,
     * and global economic organizations receive the highest weights.
     */
    private int scoreWorldEconomies(String text) {
        int score = 0;

        // Macroeconomic indicators
        if (contains(text, "gross domestic product")) {
            score += 4;
        }

        if (contains(text, "gdp")) {
            score += 4;
        }

        if (contains(text, "economic growth")) {
            score += 3;
        }

        if (contains(text, "economic slowdown")) {
            score += 3;
        }

        if (contains(text, "economic recession")) {
            score += 3;
        }

        if (contains(text, "economic recovery")) {
            score += 3;
        }

        if (contains(text, "gross national product")) {
            score += 4;
        }

        // Inflation and employment
        if (contains(text, "inflation")) {
            score += 3;
        }

        if (contains(text, "deflation")) {
            score += 3;
        }

        if (contains(text, "consumer price index")) {
            score += 3;
        }

        if (contains(text, "cpi")) {
            score += 3;
        }

        if (contains(text, "producer price index")) {
            score += 3;
        }

        if (contains(text, "ppi")) {
            score += 3;
        }

        if (contains(text, "unemployment")) {
            score += 3;
        }

        if (contains(text, "employment rate")) {
            score += 3;
        }

        if (contains(text, "labor market")) {
            score += 3;
        }

        // Central banking
        if (contains(text, "central bank")) {
            score += 4;
        }

        if (contains(text, "federal reserve")) {
            score += 4;
        }

        if (contains(text, "fed")) {
            score += 3;
        }

        if (contains(text, "interest rate")) {
            score += 3;
        }

        if (contains(text, "benchmark rate")) {
            score += 3;
        }

        if (contains(text, "monetary policy")) {
            score += 4;
        }

        if (contains(text, "quantitative easing")) {
            score += 4;
        }

        // International trade
        if (contains(text, "international trade")) {
            score += 4;
        }

        if (contains(text, "global trade")) {
            score += 4;
        }

        if (contains(text, "trade deficit")) {
            score += 4;
        }

        if (contains(text, "trade surplus")) {
            score += 4;
        }

        if (contains(text, "balance of trade")) {
            score += 4;
        }

        if (contains(text, "exports")) {
            score += 5;
        }

        if (contains(text, "imports")) {
            score += 5;
        }

        if (contains(text, "tariff")) {
            score += 5;
        }

        if (contains(text, "free trade")) {
            score += 3;
        }

        // International organizations
        if (contains(text, "international monetary fund")) {
            score += 4;
        }

        if (contains(text, "imf")) {
            score += 4;
        }

        if (contains(text, "world bank")) {
            score += 4;
        }

        if (contains(text, "world trade organization")) {
            score += 4;
        }

        if (contains(text, "wto")) {
            score += 4;
        }

        if (contains(text, "organization for economic cooperation and development")) {
            score += 4;
        }

        if (contains(text, "oecd")) {
            score += 4;
        }

        // Currency markets
        if (contains(text, "exchange rate")) {
            score += 3;
        }

        if (contains(text, "foreign exchange")) {
            score += 3;
        }

        if (contains(text, "forex")) {
            score += 3;
        }

        if (contains(text, "currency")) {
            score += 1;
        }

        if (contains(text, "devaluation")) {
            score += 3;
        }

        if (contains(text, "appreciation")) {
            score += 3;
        }

        // Economic measures
        if (contains(text, "purchasing power")) {
            score += 3;
        }

        if (contains(text, "consumer spending")) {
            score += 3;
        }

        if (contains(text, "economic outlook")) {
            score += 3;
        }

        if (contains(text, "business cycle")) {
            score += 3;
        }

        // General terms
        if (contains(text, "economy")) {
            score += 4;
        }

        if (contains(text, "economics")) {
            score += 1;
        }

        if (contains(text, "economic")) {
            score += 1;
        }

        // Growth
        if (contains(text, "per capita gdp")) {
            score += 4;
        }

        if (contains(text, "gdp per capita")) {
            score += 4;
        }

        if (contains(text, "gross value added")) {
            score += 3;
        }

        // Monetary
        if (contains(text, "money supply")) {
            score += 3;
        }

        if (contains(text, "reserve currency")) {
            score += 3;
        }

        if (contains(text, "sovereign debt")) {
            score += 3;
        }

        // Trade
        if (contains(text, "trade agreement")) {
            score += 4;
        }

        if (contains(text, "trade war")) {
            score += 5;
        }

        if (contains(text, "supply chain")) {
            score += 2;
        }

        // Development
        if (contains(text, "developing economies")) {
            score += 3;
        }

        if (contains(text, "emerging markets")) {
            score += 3;
        }

        if (contains(text, "advanced economies")) {
            score += 3;
        }

        return score;
    }

    /**
     * Scores sports-related articles.
     */
    private int scoreSports(String text) {

        int score = 0;

        // ----------------------------------------------------
        // General Sports
        // ----------------------------------------------------
        if (contains(text, "sport")) {
            score += 1;
        }

        if (contains(text, "sports")) {
            score += 1;
        }

        if (contains(text, "athlete")) {
            score += 2;
        }

        if (contains(text, "team")) {
            score += 1;
        }

        if (contains(text, "coach")) {
            score += 1;
        }

        if (contains(text, "manager")) {
            score += 1;
        }

        if (contains(text, "league")) {
            score += 1;
        }

        if (contains(text, "championship")) {
            score += 3;
        }

        if (contains(text, "tournament")) {
            score += 2;
        }

        if (contains(text, "season")) {
            score += 1;
        }

        if (contains(text, "playoff")) {
            score += 2;
        }

        if (contains(text, "playoffs")) {
            score += 2;
        }

        if (contains(text, "final")) {
            score += 2;
        }

        if (contains(text, "finals")) {
            score += 2;
        }

        if (contains(text, "semifinal")) {
            score += 2;
        }

        if (contains(text, "quarterfinal")) {
            score += 2;
        }

        if (contains(text, "champion")) {
            score += 3;
        }

        if (contains(text, "victory")) {
            score += 2;
        }

        if (contains(text, "defeat")) {
            score += 2;
        }

        if (contains(text, "win")) {
            score += 1;
        }

        if (contains(text, "loss")) {
            score += 1;
        }

        // ----------------------------------------------------
        // Football / Soccer
        // ----------------------------------------------------
        if (contains(text, "football")) {
            score += 3;
        }

        if (contains(text, "soccer")) {
            score += 3;
        }

        if (contains(text, "fifa")) {
            score += 4;
        }

        if (contains(text, "uefa")) {
            score += 4;
        }

        if (contains(text, "premier league")) {
            score += 5;
        }

        if (contains(text, "champions league")) {
            score += 5;
        }

        if (contains(text, "world cup")) {
            score += 5;
        }

        if (contains(text, "goal")) {
            score += 2;
        }

        if (contains(text, "goalkeeper")) {
            score += 3;
        }

        if (contains(text, "striker")) {
            score += 3;
        }

        if (contains(text, "midfielder")) {
            score += 3;
        }

        if (contains(text, "defender")) {
            score += 3;
        }

        if (contains(text, "penalty")) {
            score += 3;
        }

        if (contains(text, "offside")) {
            score += 3;
        }

        // ----------------------------------------------------
        // Basketball
        // ----------------------------------------------------
        if (contains(text, "basketball")) {
            score += 3;
        }

        if (contains(text, "nba")) {
            score += 5;
        }

        if (contains(text, "wnba")) {
            score += 5;
        }

        if (contains(text, "ncaa")) {
            score += 3;
        }

        if (contains(text, "slam dunk")) {
            score += 4;
        }

        if (contains(text, "three pointer")) {
            score += 4;
        }

        if (contains(text, "rebound")) {
            score += 3;
        }

        if (contains(text, "assist")) {
            score += 2;
        }

        // ----------------------------------------------------
        // American Football
        // ----------------------------------------------------
        if (contains(text, "nfl")) {
            score += 5;
        }

        if (contains(text, "super bowl")) {
            score += 5;
        }

        if (contains(text, "touchdown")) {
            score += 4;
        }

        if (contains(text, "quarterback")) {
            score += 4;
        }

        if (contains(text, "linebacker")) {
            score += 4;
        }

        if (contains(text, "running back")) {
            score += 4;
        }

        // ----------------------------------------------------
        // Baseball
        // ----------------------------------------------------
        if (contains(text, "baseball")) {
            score += 3;
        }

        if (contains(text, "mlb")) {
            score += 5;
        }

        if (contains(text, "home run")) {
            score += 4;
        }

        if (contains(text, "pitcher")) {
            score += 3;
        }

        if (contains(text, "inning")) {
            score += 3;
        }

        // ----------------------------------------------------
        // Hockey
        // ----------------------------------------------------
        if (contains(text, "hockey")) {
            score += 3;
        }

        if (contains(text, "nhl")) {
            score += 5;
        }

        if (contains(text, "stanley cup")) {
            score += 5;
        }

        if (contains(text, "puck")) {
            score += 3;
        }

        // ----------------------------------------------------
        // Motorsports
        // ----------------------------------------------------
        if (contains(text, "formula one")) {
            score += 5;
        } else if (contains(text, "formula 1")) {
            score += 5;
        } else if (contains(text, "f1")) {
            score += 5;
        }

        if (contains(text, "grand prix")) {
            score += 5;
        }

        if (contains(text, "indycar")) {
            score += 5;
        }

        if (contains(text, "nascar")) {
            score += 5;
        }

        if (contains(text, "motogp")) {
            score += 5;
        }

        if (contains(text, "race")) {
            score += 2;
        }

        if (contains(text, "racing")) {
            score += 2;
        }

        if (contains(text, "pole position")) {
            score += 4;
        }

        if (contains(text, "lap")) {
            score += 2;
        }

        if (contains(text, "pit stop")) {
            score += 4;
        }

        // ----------------------------------------------------
        // Tennis
        // ----------------------------------------------------
        if (contains(text, "tennis")) {
            score += 3;
        }

        if (contains(text, "wimbledon")) {
            score += 5;
        }

        if (contains(text, "us open")) {
            score += 5;
        }

        if (contains(text, "french open")) {
            score += 5;
        }

        if (contains(text, "australian open")) {
            score += 5;
        }

        if (contains(text, "grand slam")) {
            score += 5;
        }

        if (contains(text, "ace")) {
            score += 2;
        }

        if (contains(text, "set")) {
            score += 1;
        }

        if (contains(text, "match point")) {
            score += 4;
        }

        // ----------------------------------------------------
        // Golf
        // ----------------------------------------------------
        if (contains(text, "golf")) {
            score += 3;
        }

        if (contains(text, "pga")) {
            score += 5;
        }

        if (contains(text, "masters")) {
            score += 4;
        }

        if (contains(text, "birdie")) {
            score += 3;
        }

        if (contains(text, "eagle")) {
            score += 3;
        }

        if (contains(text, "bogey")) {
            score += 3;
        }

        // ----------------------------------------------------
        // Combat Sports
        // ----------------------------------------------------
        if (contains(text, "boxing")) {
            score += 3;
        }

        if (contains(text, "ufc")) {
            score += 5;
        }

        if (contains(text, "mma")) {
            score += 5;
        }

        if (contains(text, "knockout")) {
            score += 4;
        }

        if (contains(text, "heavyweight")) {
            score += 3;
        }

        if (contains(text, "welterweight")) {
            score += 3;
        }

        // ----------------------------------------------------
        // Cycling
        // ----------------------------------------------------
        if (contains(text, "tour de france")) {
            score += 5;
        }

        if (contains(text, "giro d'italia")) {
            score += 5;
        }

        if (contains(text, "vuelta")) {
            score += 5;
        }

        if (contains(text, "cycling")) {
            score += 3;
        }

        if (contains(text, "cyclist")) {
            score += 3;
        }

        if (contains(text, "peloton")) {
            score += 4;
        }

        // ----------------------------------------------------
        // Olympics
        // ----------------------------------------------------
        if (contains(text, "olympics")) {
            score += 5;
        }

        if (contains(text, "olympic")) {
            score += 5;
        }

        if (contains(text, "paralympics")) {
            score += 5;
        }

        if (contains(text, "paralympic")) {
            score += 5;
        }

        if (contains(text, "gold medal")) {
            score += 4;
        }

        if (contains(text, "silver medal")) {
            score += 4;
        }

        if (contains(text, "bronze medal")) {
            score += 4;
        }

        // ----------------------------------------------------
        // Formula One Drivers
        // ----------------------------------------------------
        if (contains(text, "hamilton")) {
            score += 4;
        }

        if (contains(text, "verstappen")) {
            score += 4;
        }

        if (contains(text, "norris")) {
            score += 4;
        }

        if (contains(text, "leclerc")) {
            score += 4;
        }

        if (contains(text, "russell")) {
            score += 4;
        }

        if (contains(text, "piastri")) {
            score += 4;
        }

        // ----------------------------------------------------
        // Soccer Players
        // ----------------------------------------------------
        if (contains(text, "messi")) {
            score += 4;
        }

        if (contains(text, "ronaldo")) {
            score += 4;
        }

        if (contains(text, "mbappe")) {
            score += 4;
        }

        if (contains(text, "haaland")) {
            score += 4;
        }

        // ----------------------------------------------------
        // Basketball Players
        // ----------------------------------------------------
        if (contains(text, "lebron")) {
            score += 4;
        }

        if (contains(text, "curry")) {
            score += 4;
        }

        if (contains(text, "durant")) {
            score += 4;
        }

        if (contains(text, "jokic")) {
            score += 4;
        }

        // ----------------------------------------------------
        // Golfers
        // ----------------------------------------------------
        if (contains(text, "scheffler")) {
            score += 4;
        }

        if (contains(text, "mcilroy")) {
            score += 4;
        }

        // ----------------------------------------------------
        // Cyclists
        // ----------------------------------------------------
        if (contains(text, "pogacar")) {
            score += 4;
        }

        if (contains(text, "vingegaard")) {
            score += 4;
        }

        return score;
    }

    /**
     * Scores entertainment-related articles.
     */
    private int scoreEntertainment(String text) {

        int score = 0;

        // ----------------------------------------------------
        // Movies & Film
        // ----------------------------------------------------
        if (contains(text, "movie")) {
            score += 1;
        }

        if (contains(text, "movies")) {
            score += 1;
        }

        if (contains(text, "film")) {
            score += 1;
        }

        if (contains(text, "films")) {
            score += 1;
        }

        if (contains(text, "cinema")) {
            score += 2;
        }

        if (contains(text, "box office")) {
            score += 4;
        }

        if (contains(text, "director")) {
            score += 2;
        }

        if (contains(text, "producer")) {
            score += 2;
        }

        if (contains(text, "screenwriter")) {
            score += 3;
        }

        if (contains(text, "screenplay")) {
            score += 3;
        }

        if (contains(text, "actor")) {
            score += 2;
        }

        if (contains(text, "actors")) {
            score += 2;
        }

        if (contains(text, "actress")) {
            score += 2;
        }

        if (contains(text, "cast")) {
            score += 2;
        }

        if (contains(text, "premiere")) {
            score += 3;
        }

        if (contains(text, "blockbuster")) {
            score += 3;
        }

        if (contains(text, "sequel")) {
            score += 3;
        }

        if (contains(text, "franchise")) {
            score += 2;
        }

        if (contains(text, "trailer")) {
            score += 3;
        }

        // ----------------------------------------------------
        // Television
        // ----------------------------------------------------
        if (contains(text, "television")) {
            score += 2;
        }

        if (contains(text, "tv series")) {
            score += 3;
        }

        if (contains(text, "series")) {
            score += 1;
        }

        if (contains(text, "episode")) {
            score += 2;
        }

        if (contains(text, "season finale")) {
            score += 4;
        }

        if (contains(text, "sitcom")) {
            score += 3;
        }

        if (contains(text, "drama")) {
            score += 2;
        }

        if (contains(text, "reality show")) {
            score += 3;
        }

        if (contains(text, "talk show")) {
            score += 3;
        }

        // ----------------------------------------------------
        // Streaming
        // ----------------------------------------------------
        if (contains(text, "streaming")) {
            score += 1;
        }

        if (contains(text, "streaming service")) {
            score += 3;
        }

        if (contains(text, "streaming series")) {
            score += 3;
        }

        if (contains(text, "netflix")) {
            score += 5;
        }

        if (contains(text, "disney+")) {
            score += 5;
        }

        if (contains(text, "hulu")) {
            score += 5;
        }

        if (contains(text, "amazon prime")) {
            score += 5;
        }

        if (contains(text, "prime video")) {
            score += 5;
        }

        if (contains(text, "apple tv+")) {
            score += 5;
        }

        if (contains(text, "max")) {
            score += 5;
        }

        if (contains(text, "paramount+")) {
            score += 5;
        }

        if (contains(text, "peacock")) {
            score += 5;
        }

        // ----------------------------------------------------
        // Music
        // ----------------------------------------------------
        if (contains(text, "music")) {
            score += 1;
        }

        if (contains(text, "musician")) {
            score += 2;
        }

        if (contains(text, "singer")) {
            score += 2;
        }

        if (contains(text, "band")) {
            score += 2;
        }

        if (contains(text, "album")) {
            score += 3;
        }

        if (contains(text, "single")) {
            score += 2;
        }

        if (contains(text, "concert")) {
            score += 3;
        }

        /*
        * Use an else-if chain so an article containing "concert tour"
        * or "world tour" does not also receive the generic "tour" score.
        */
        if (contains(text, "concert tour")) {
            score += 4;
        } else if (contains(text, "world tour")) {
            score += 3;
        } else if (contains(text, "tour")) {
            score += 1;
        }

        if (contains(text, "music festival")) {
            score += 3;
        } else if (contains(text, "festival")) {
            score += 1;
        }

        if (contains(text, "orchestra")) {
            score += 2;
        }

        if (contains(text, "composer")) {
            score += 2;
        }

        if (contains(text, "record label")) {
            score += 4;
        }

        if (contains(text, "billboard")) {
            score += 4;
        }

        if (contains(text, "spotify")) {
            score += 4;
        }

        if (contains(text, "apple music")) {
            score += 4;
        }

        // ----------------------------------------------------
        // Entertainment Awards
        // ----------------------------------------------------
        if (contains(text, "oscars")) {
            score += 5;
        }

        if (contains(text, "academy awards")) {
            score += 5;
        }

        if (contains(text, "emmys")) {
            score += 5;
        }

        if (contains(text, "grammys")) {
            score += 5;
        }

        if (contains(text, "golden globes")) {
            score += 5;
        }

        if (contains(text, "bafta")) {
            score += 5;
        }

        if (contains(text, "cannes")) {
            score += 5;
        }

        if (contains(text, "tony awards")) {
            score += 5;
        }

        // ----------------------------------------------------
        // Celebrity & Live Entertainment
        // ----------------------------------------------------
        if (contains(text, "celebrity")) {
            score += 2;
        }

        if (contains(text, "hollywood")) {
            score += 4;
        }

        if (contains(text, "broadway")) {
            score += 4;
        }

        if (contains(text, "red carpet")) {
            score += 4;
        }

        if (contains(text, "paparazzi")) {
            score += 4;
        }

        if (contains(text, "musical")) {
            score += 2;
        }

        if (contains(text, "stage production")) {
            score += 3;
        }

        // ----------------------------------------------------
        // Video Games
        // ----------------------------------------------------
        if (contains(text, "video game")) {
            score += 3;
        }

        if (contains(text, "video games")) {
            score += 3;
        }

        if (contains(text, "gaming")) {
            score += 3;
        }

        if (contains(text, "playstation")) {
            score += 4;
        }

        if (contains(text, "xbox")) {
            score += 4;
        }

        if (contains(text, "nintendo")) {
            score += 4;
        }

        if (contains(text, "steam")) {
            score += 4;
        }

        if (contains(text, "esports")) {
            score += 5;
        }

        if (contains(text, "game developer")) {
            score += 3;
        }

        // ----------------------------------------------------
        // Musicians & Performers
        // ----------------------------------------------------
        if (contains(text, "taylor swift")) {
            score += 5;
        }

        if (contains(text, "beyonce")) {
            score += 5;
        }

        if (contains(text, "adele")) {
            score += 5;
        }

        if (contains(text, "lady gaga")) {
            score += 5;
        }

        if (contains(text, "drake")) {
            score += 5;
        }

        if (contains(text, "the weeknd")) {
            score += 5;
        }

        // ----------------------------------------------------
        // Actors
        // ----------------------------------------------------
        if (contains(text, "tom cruise")) {
            score += 5;
        }

        if (contains(text, "brad pitt")) {
            score += 5;
        }

        if (contains(text, "leonardo dicaprio")) {
            score += 5;
        }

        if (contains(text, "zendaya")) {
            score += 5;
        }

        if (contains(text, "ryan reynolds")) {
            score += 5;
        }

        // ----------------------------------------------------
        // Directors
        // ----------------------------------------------------
        if (contains(text, "steven spielberg")) {
            score += 5;
        }

        if (contains(text, "christopher nolan")) {
            score += 5;
        }

        return score;
    }

    /**
 * Scores culture-related articles.
 */
private int scoreCulture(String text) {

    int score = 0;

    // ----------------------------------------------------
    // Art
    // ----------------------------------------------------
    if (contains(text, "art")) {
        score += 1;
    }

    if (contains(text, "artist")) {
        score += 2;
    }

    if (contains(text, "painting")) {
        score += 3;
    }

    if (contains(text, "sculpture")) {
        score += 3;
    }

    if (contains(text, "portrait")) {
        score += 2;
    }

    if (contains(text, "gallery")) {
        score += 3;
    }

    if (contains(text, "museum")) {
        score += 4;
    }

    if (contains(text, "exhibit")) {
        score += 3;
    }

    if (contains(text, "exhibition")) {
        score += 3;
    }

    if (contains(text, "architecture")) {
        score += 1;
    }

    // ----------------------------------------------------
    // Literature
    // ----------------------------------------------------
    if (contains(text, "book")) {
        score += 1;
    }

    if (contains(text, "books")) {
        score += 1;
    }

    if (contains(text, "novel")) {
        score += 3;
    }

    if (contains(text, "author")) {
        score += 3;
    }

    if (contains(text, "poetry")) {
        score += 3;
    }

    if (contains(text, "poem")) {
        score += 2;
    }

    if (contains(text, "literature")) {
        score += 4;
    }

    if (contains(text, "publisher")) {
        score += 2;
    }

    if (contains(text, "publishing")) {
        score += 2;
    }

    if (contains(text, "library")) {
        score += 3;
    }

    // ----------------------------------------------------
    // History
    // ----------------------------------------------------
    if (contains(text, "history")) {
        score += 1;
    }

    if (contains(text, "historical")) {
        score += 3;
    }

    if (contains(text, "historic")) {
        score += 3;
    }

    if (contains(text, "civilization")) {
        score += 4;
    }

    if (contains(text, "empire")) {
        score += 3;
    }

    if (contains(text, "ancient")) {
        score += 3;
    }

    if (contains(text, "medieval")) {
        score += 3;
    }

    if (contains(text, "heritage")) {
        score += 3;
    }

    if (contains(text, "archive")) {
        score += 2;
    }

    // ----------------------------------------------------
    // Archaeology
    // ----------------------------------------------------
    if (contains(text, "archaeology")) {
        score += 4;
    }

    if (contains(text, "archaeologist")) {
        score += 4;
    }

    if (contains(text, "artifact")) {
        score += 4;
    }

    if (contains(text, "artifacts")) {
        score += 4;
    }

    if (contains(text, "excavation")) {
        score += 4;
    }

    if (contains(text, "ruins")) {
        score += 3;
    }

    if (contains(text, "fossil")) {
        score += 3;
    }

    if (contains(text, "dig site")) {
        score += 4;
    }

    // ----------------------------------------------------
    // Religion
    // ----------------------------------------------------
    if (contains(text, "religion")) {
        score += 3;
    }

    if (contains(text, "faith")) {
        score += 2;
    }

    if (contains(text, "church")) {
        score += 2;
    }

    if (contains(text, "cathedral")) {
        score += 3;
    }

    if (contains(text, "mosque")) {
        score += 3;
    }

    if (contains(text, "synagogue")) {
        score += 3;
    }

    if (contains(text, "temple")) {
        score += 3;
    }

    if (contains(text, "pope")) {
        score += 5;
    }

    if (contains(text, "bishop")) {
        score += 3;
    }

    if (contains(text, "vatican")) {
        score += 5;
    }

    if (contains(text, "pilgrimage")) {
        score += 4;
    }

    if (contains(text, "christianity")) {
        score += 4;
    }

    if (contains(text, "islam")) {
        score += 4;
    }

    if (contains(text, "judaism")) {
        score += 4;
    }

    if (contains(text, "buddhism")) {
        score += 4;
    }

    if (contains(text, "hinduism")) {
        score += 4;
    }

    // ----------------------------------------------------
    // Language
    // ----------------------------------------------------
    if (contains(text, "language")) {
        score += 2;
    }

    if (contains(text, "linguistics")) {
        score += 4;
    }

    if (contains(text, "dialect")) {
        score += 3;
    }

    if (contains(text, "translation")) {
        score += 3;
    }

    // ----------------------------------------------------
    // Traditions
    // ----------------------------------------------------
    if (contains(text, "tradition")) {
        score += 3;
    }

    if (contains(text, "custom")) {
        score += 2;
    }

    if (contains(text, "folklore")) {
        score += 4;
    }

    if (contains(text, "ceremony")) {
        score += 3;
    }

    if (contains(text, "ritual")) {
        score += 3;
    }

    if (contains(text, "indigenous")) {
        score += 4;
    }

    if (contains(text, "unesco")) {
        score += 5;
    }

    if (contains(text, "world heritage")) {
        score += 5;
    }

    // ----------------------------------------------------
    // Museums
    // ----------------------------------------------------
    if (contains(text, "smithsonian")) {
        score += 5;
    }

    if (contains(text, "british museum")) {
        score += 5;
    }

    if (contains(text, "louvre")) {
        score += 5;
    }

    if (contains(text, "metropolitan museum")) {
        score += 5;
    }

    // ----------------------------------------------------
    // Authors
    // ----------------------------------------------------
    if (contains(text, "stephen king")) {
        score += 5;
    }

    if (contains(text, "jk rowling")) {
        score += 5;
    }

    if (contains(text, "george rr martin")) {
        score += 5;
    }

    if (contains(text, "margaret atwood")) {
        score += 5;
    }

    if (contains(text, "neil gaiman")) {
        score += 5;
    }

    return score;
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