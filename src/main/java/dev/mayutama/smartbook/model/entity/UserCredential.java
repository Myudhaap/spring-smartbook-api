package dev.mayutama.smartbook.model.entity;

import dev.mayutama.smartbook.common.entity.BaseEntity;
import dev.mayutama.smartbook.constant.DbPath;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Entity
@Table(name = DbPath.MST_USER_CREDENTIAL_SCHEMA)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UserCredential extends BaseEntity {
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = DbPath.TRX_USER_CREDENTIAL_ROLE_SCHEMA,
            joinColumns = @JoinColumn(name = "user_credential_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @OneToOne(mappedBy = "userCredential", cascade = CascadeType.ALL)
    private User user;
}
