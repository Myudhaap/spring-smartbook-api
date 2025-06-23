package dev.mayutama.smartbook.repository;

import dev.mayutama.smartbook.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}
