package dev.mayutama.smartbook.model.dto.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ActiveUserStatsRes {
    private String fullName;
    private Long totalReviews;
}
