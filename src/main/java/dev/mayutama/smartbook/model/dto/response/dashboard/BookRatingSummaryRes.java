package dev.mayutama.smartbook.model.dto.response.dashboard;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class BookRatingSummaryRes {
    private String id;
    private String title;
    private Double averageRating;
    private Long totalReviews;
}
