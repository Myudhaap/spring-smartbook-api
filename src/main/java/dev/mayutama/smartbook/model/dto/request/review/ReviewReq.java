package dev.mayutama.smartbook.model.dto.request.review;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ReviewReq {
    @NotNull(message = "Field rating is required")
    @Min(value = 1, message = "Field rating have minimum value is 1")
    @Max(value = 5, message = "Field rating have maximum value is 5")
    private Integer rating;
    @NotNull(message = "Field comment is required")
    private String comment;
    @NotNull(message = "Field bookId is required")
    private String bookId;
}
