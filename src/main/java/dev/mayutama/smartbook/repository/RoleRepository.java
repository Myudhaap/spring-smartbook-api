package dev.mayutama.smartbook.repository;

import dev.mayutama.smartbook.common.repository.BaseRepository;
import dev.mayutama.smartbook.model.entity.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface RoleRepository extends BaseRepository<Role, String> {
    @Query(
            value = """
            SELECT  * FROM mst_roles
            WHERE id = :id AND is_active = true
        """,
            nativeQuery = true
    )
    Optional<Role> findByIdIsActive(@Param("id") String id);

    Set<Role> findAllByIdIn(Set<String> ids);
    @Query(value = """
        SELECT * FROM mst_role
        WHERE name = :name AND is_active = true
    """,
    nativeQuery = true)
    Optional<Role> findByName(@Param("name") String name);
}
