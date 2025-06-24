package dev.mayutama.smartbook.service;

import dev.mayutama.smartbook.model.dto.response.user.UserResponse;
import dev.mayutama.smartbook.model.entity.User;

public interface UserService {
    User addUserRegister(User request);
    User findEntityById(String id);
    UserResponse findByUserCredentialId(String userCredentialId);
}
