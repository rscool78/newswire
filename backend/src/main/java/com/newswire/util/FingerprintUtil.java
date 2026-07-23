package com.newswire.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Locale;

public final class FingerprintUtil {

    private FingerprintUtil() {}

    public static String articleFingerprint(String url, String title) {
        String normalizedUrl = normalizeUrl(url);
        String normalizedTitle = normalizeTitle(title);

        return sha256(normalizedUrl + "|" + normalizedTitle);
    }

    public static String sha256(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(
                    input.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(digest);
        } catch (Exception e) {
            throw new RuntimeException(
                    "Unable to hash fingerprint",
                    e
            );
        }
    }

    private static String normalizeUrl(String url) {
        return url == null
                ? ""
                : url.trim().toLowerCase(Locale.ROOT);
    }

    private static String normalizeTitle(String title) {
        return title == null
                ? ""
                : title
                    .trim()
                    .replaceAll("\\s+", " ")
                    .toLowerCase(Locale.ROOT);
    }
}