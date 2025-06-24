package dev.mayutama.smartbook.model.entity;

import dev.mayutama.smartbook.common.entity.BaseEntity;
import dev.mayutama.smartbook.constant.DbPath;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = DbPath.MST_BOOK_SCHEMA)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Book extends BaseEntity {
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "author")
    private String author;
    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "published_at")
    private Long publishedAt;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = DbPath.TRX_BOOK_GENRE_SCHEMA,
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    Set<Genre> genres;
}
