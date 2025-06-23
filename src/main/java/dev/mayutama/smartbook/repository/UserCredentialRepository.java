package dev.mayutama.smartbook.repository;

import dev.mayutama.smartbook.model.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserCredentialRepository extends JpaRepository<UserCredential, String> {
}
