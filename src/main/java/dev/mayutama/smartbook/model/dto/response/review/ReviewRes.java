package dev.mayutama.smartbook.model.dto.response.review;

import dev.mayutama.smartbook.common.response.BaseResponse;
import dev.mayutama.smartbook.model.dto.response.book.BookRes;
import dev.mayutama.smartbook.model.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRes extends BaseResponse {
    private String id;
    private Integer rating;
    private String comment;
    private Boolean isVerified;
    private Double sentimentScore;
    private String userName;
    private BookRes book;
}
