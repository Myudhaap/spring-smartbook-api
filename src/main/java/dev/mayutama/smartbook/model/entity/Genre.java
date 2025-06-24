package dev.mayutama.smartbook.model.entity;

import dev.mayutama.smartbook.common.entity.BaseEntity;
import dev.mayutama.smartbook.constant.DbPath;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = DbPath.MST_GENRE_SCHEMA)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Genre extends BaseEntity {
    @Column(name = "name", unique = true)
    private String name;
    @Column(name = "description")
    private String description;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;
}
