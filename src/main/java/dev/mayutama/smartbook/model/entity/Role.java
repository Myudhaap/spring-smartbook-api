package dev.mayutama.smartbook.model.entity;

import dev.mayutama.smartbook.common.entity.BaseEntity;
import dev.mayutama.smartbook.constant.DbPath;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = DbPath.MST_ROLE_SCHEMA)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Role extends BaseEntity {
    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = DbPath.TRX_ROLE_PERMISSION_SCHEMA,
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    Set<Permission> permissions;
}
