package dev.mayutama.smartbook.repository;

import dev.mayutama.smartbook.common.repository.BaseRepository;
import dev.mayutama.smartbook.model.entity.Book;
import dev.mayutama.smartbook.model.projection.BookRatingSummary;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends BaseRepository<Book, String> {
    Optional<Book> findByIdAndIsActiveTrue(String id);

    @Query(
        value = """
            SELECT b.id, b.title, AVG(r.rating) AS avg_rating, COUNT(r.id) AS total_reviews
            FROM mst_book b
            JOIN trx_review r ON b.id = r.book_id
            GROUP BY b.id, b.title
            ORDER BY avg_rating DESC
            LIMIT :limit
        """,
            nativeQuery = true
    )
    List<BookRatingSummary> findTopRated(@Param("limit") Integer limit);
}
