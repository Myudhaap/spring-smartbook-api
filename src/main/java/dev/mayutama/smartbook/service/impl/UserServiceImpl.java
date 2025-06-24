package dev.mayutama.smartbook.service.impl;

import dev.mayutama.smartbook.exception.ApplicationException;
import dev.mayutama.smartbook.model.dto.response.user.UserResponse;
import dev.mayutama.smartbook.model.entity.User;
import dev.mayutama.smartbook.model.mapper.UserMapper;
import dev.mayutama.smartbook.repository.UserRepository;
import dev.mayutama.smartbook.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final UserMapper userMapper;

    @Override
    public User addUserRegister(User request) {
        userRepository.saveAndFlush(request);
        return request;
    }

    @Override
    public User findEntityById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(
                        null,
                        "User not found",
                        HttpStatus.NOT_FOUND
                ));
    }

    @Override
    public UserResponse findByUserCredentialId(String userCredentialId) {
        User user = userRepository.findByUserCredential_Id(userCredentialId)
                .orElseThrow(() -> new ApplicationException(
                        HttpStatus.NOT_FOUND.name(),
                        "Data not found",
                        HttpStatus.NOT_FOUND
                ));
        return userMapper.toRes(user);
    }
}
