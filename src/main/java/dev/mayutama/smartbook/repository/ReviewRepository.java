package dev.mayutama.smartbook.repository;

import dev.mayutama.smartbook.common.repository.BaseRepository;
import dev.mayutama.smartbook.model.entity.Review;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends BaseRepository<Review, String> {
    Optional<Review> findByBookIdAndUserId(String bookId, String userId);
    List<Review> findAllByBookId(String bookId);
}
