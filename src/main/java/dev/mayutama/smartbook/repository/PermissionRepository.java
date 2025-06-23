package dev.mayutama.smartbook.repository;

import dev.mayutama.smartbook.common.repository.BaseRepository;
import dev.mayutama.smartbook.model.entity.Permission;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PermissionRepository extends BaseRepository<Permission, String> {
    @Query(
        value = """
            SELECT  * FROM mst_permissions
            WHERE id = :id AND is_active = true
        """,
        nativeQuery = true
    )
    Optional<Permission> findByIdIsActive(@Param("id") String id);
}
