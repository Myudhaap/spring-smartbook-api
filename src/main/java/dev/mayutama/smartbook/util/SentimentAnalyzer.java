package dev.mayutama.smartbook.util;

import java.util.Set;

public class SentimentAnalyzer {
    private static final Set<String> POSITIVE = Set.of(
            "baik", "bagus", "senang", "luar biasa", "puas", "mantap"
    );

    private static final Set<String> NEGATIVE = Set.of(
            "buruk", "jelek", "kecewa", "mengecewakan", "parah", "tidak puas"
    );

    public static double analyze(String text) {
        double score = 0.0;
        String[] words = text.toLowerCase().replaceAll("[^a-zA-Z\\s]", "").split("\\s+");

        for (String word : words) {
            if (POSITIVE.contains(word)) score += 0.1;
            else if (NEGATIVE.contains(word)) score -= 0.1;
        }

        return score;
    }
}
