package dev.mayutama.smartbook.repository;

import dev.mayutama.smartbook.common.repository.BaseRepository;
import dev.mayutama.smartbook.model.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends BaseRepository<User, String> {
    @Query(
            value = """
            SELECT  * FROM mst_user
            WHERE id = :id AND is_active = true
        """,
            nativeQuery = true
    )
    Optional<User> findByIdIsActive(@Param("id") String id);
    Optional<User> findByUserCredential_Id(String id);
}
