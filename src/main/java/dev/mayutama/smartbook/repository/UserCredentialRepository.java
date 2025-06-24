package dev.mayutama.smartbook.repository;

import dev.mayutama.smartbook.common.repository.BaseRepository;
import dev.mayutama.smartbook.model.entity.UserCredential;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserCredentialRepository extends BaseRepository<UserCredential, String> {
    @Query(
            value = """
            SELECT  * FROM mst_user_credential
            WHERE id = :id AND is_active = true
        """,
            nativeQuery = true
    )
    Optional<UserCredential> findByIdIsActive(@Param("id") String id);

    Optional<UserCredential> findByEmailAndIsActive(String email, Boolean isActive);
    Optional<UserCredential> findByUser_Id(String id);
}
