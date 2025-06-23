package dev.mayutama.smartbook.repository;

import dev.mayutama.smartbook.common.repository.BaseRepository;
import dev.mayutama.smartbook.model.entity.Permission;
import dev.mayutama.smartbook.model.entity.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends BaseRepository<Role, String> {
    @Query(
            value = """
            SELECT  * FROM mst_roles
            WHERE id = :id AND is_active = true
        """,
            nativeQuery = true
    )
    Optional<Permission> findByIdIsActive(@Param("id") String id);
}
