package dev.mayutama.smartbook.repository;

import dev.mayutama.smartbook.common.repository.BaseRepository;
import dev.mayutama.smartbook.model.entity.Genre;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface GenreRepository extends BaseRepository<Genre, String> {
    @Query(
        value = """
            SELECT * FROM mst_genre
            WHERE id = :id AND is_active = true
        """,
        nativeQuery = true
    )
    Optional<Genre> findByIdActive(@Param("id") String id);

    Set<Genre> findAllByIdIn(Set<String> ids);
}
