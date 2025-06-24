package dev.mayutama.smartbook.model.entity;

import dev.mayutama.smartbook.common.entity.BaseEntity;
import dev.mayutama.smartbook.constant.DbPath;
import dev.mayutama.smartbook.constant.EGender;
import dev.mayutama.smartbook.constant.EMaritalStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = DbPath.MST_USER_SCHEMA)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class User extends BaseEntity {
    @Column(name = "fullname", nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false)
    private EGender gender;

    @Column(name = "birth_date")
    private Long birthDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "marital_status", nullable = false)
    private EMaritalStatus maritalStatus;

    @OneToOne
    @JoinColumn(name = "user_credential_id")
    private UserCredential userCredential;
}
