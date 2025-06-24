package dev.mayutama.smartbook.repository;

import dev.mayutama.smartbook.common.repository.BaseRepository;
import dev.mayutama.smartbook.model.entity.Book;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends BaseRepository<Book, String> {
    Optional<Book> findByIdAndIsActiveTrue(String id);
}
