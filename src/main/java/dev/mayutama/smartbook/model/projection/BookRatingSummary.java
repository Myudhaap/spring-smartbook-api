package dev.mayutama.smartbook.model.projection;

public interface BookRatingSummary {
    String getId();
    String getTitle();
    Double getAvgRating();
    Long getTotalReviews();
}
