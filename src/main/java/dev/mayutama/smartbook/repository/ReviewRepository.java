package dev.mayutama.smartbook.repository;

import dev.mayutama.smartbook.common.repository.BaseRepository;
import dev.mayutama.smartbook.model.entity.Review;
import dev.mayutama.smartbook.model.projection.ActiveUserStats;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends BaseRepository<Review, String> {
    Optional<Review> findByBookIdAndUserId(String bookId, String userId);
    List<Review> findAllByBookId(String bookId);

    @Query(
        value = """
            SELECT u.id, u.fullname, COUNT(r.id) AS total_reviews
            FROM trx_review r
            JOIN mst_user u ON u.id = r.user_id
            GROUP BY u.id, u.id, u.fullname
            ORDER BY total_reviews DESC
            LIMIT :limit
        """,
        nativeQuery = true
    )
    List<ActiveUserStats> findMostActiveUser(@Param("limit") Integer limit);
}
