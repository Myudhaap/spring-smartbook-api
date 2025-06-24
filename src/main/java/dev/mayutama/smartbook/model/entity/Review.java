package dev.mayutama.smartbook.model.entity;

import dev.mayutama.smartbook.common.entity.BaseEntity;
import dev.mayutama.smartbook.constant.DbPath;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

@Entity
@Table(
    name = DbPath.TRX_REVIEW_SCHEMA,
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "book_id"})
    }
)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Review extends BaseEntity {
    @Column(name = "rating", nullable = false)
    @Check(constraints = "rating <= 5 AND rating >= 1")
    private Integer rating;
    @Column(name = "comment", nullable = false)
    @Lob
    @Basic(fetch = FetchType.EAGER)
    private String comment;
    @Column(name = "is_verified")
    @Builder.Default
    private Boolean isVerified = false;
    @Column(name = "sentiment_score")
    private Double sentimentScore;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
