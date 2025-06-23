package dev.mayutama.smartbook.model.entity;

import dev.mayutama.smartbook.common.entity.BaseEntity;
import dev.mayutama.smartbook.constant.DbPath;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = DbPath.MST_PERMISSION_SCHEMA)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class Permission extends BaseEntity {
    @Column(name = "name", unique = true)
    private String name;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
