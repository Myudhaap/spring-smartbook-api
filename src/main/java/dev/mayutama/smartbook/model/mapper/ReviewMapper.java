package dev.mayutama.smartbook.model.mapper;

import dev.mayutama.smartbook.model.dto.request.review.ReviewReq;
import dev.mayutama.smartbook.model.dto.response.book.BookRes;
import dev.mayutama.smartbook.model.dto.response.review.ReviewRes;
import dev.mayutama.smartbook.model.entity.Book;
import dev.mayutama.smartbook.model.entity.Review;
import dev.mayutama.smartbook.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReviewMapper {
    private final BookMapper bookMapper;

    public Review toEntity(ReviewReq req, Book book, User user){
        return Review.builder()
                .rating(req.getRating())
                .comment(req.getComment())
                .book(book)
                .user(user)
                .build();
    }

    public ReviewRes toRes(Review review) {
        BookRes bookRes = bookMapper.toRes(review.getBook());
        return ReviewRes.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .isVerified(review.getIsVerified())
                .sentimentScore(review.getSentimentScore())
                .book(bookRes)
                .userName(review.getUser().getFullName())
                .createdBy(review.getCreatedBy())
                .createdAt(review.getCreatedAt())
                .updatedBy(review.getUpdatedBy())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
