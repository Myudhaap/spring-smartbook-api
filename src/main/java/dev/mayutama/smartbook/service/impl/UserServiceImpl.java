package dev.mayutama.smartbook.service.impl;

import dev.mayutama.smartbook.common.repository.BaseRepository;
import dev.mayutama.smartbook.common.rest.RestFilter;
import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.common.service.EngineFilterService;
import dev.mayutama.smartbook.exception.ApplicationException;
import dev.mayutama.smartbook.model.dto.request.user.UserReq;
import dev.mayutama.smartbook.model.dto.response.user.UserRes;
import dev.mayutama.smartbook.model.entity.Role;
import dev.mayutama.smartbook.model.entity.User;
import dev.mayutama.smartbook.model.entity.UserCredential;
import dev.mayutama.smartbook.model.mapper.UserMapper;
import dev.mayutama.smartbook.repository.UserRepository;
import dev.mayutama.smartbook.service.AuthService;
import dev.mayutama.smartbook.service.RoleService;
import dev.mayutama.smartbook.service.UserCredentialService;
import dev.mayutama.smartbook.service.UserService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl
    extends EngineFilterService<User, String>
    implements UserService {
    private final UserRepository userRepository;
    private final UserCredentialService userCredentialService;
    private final AuthService authService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

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
    public UserRes findByUserCredentialId(String userCredentialId) {
        User user = userRepository.findByUserCredential_Id(userCredentialId)
                .orElseThrow(() -> new ApplicationException(
                        HttpStatus.NOT_FOUND.name(),
                        "Data not found",
                        HttpStatus.NOT_FOUND
                ));
        return userMapper.toRes(user);
    }

    @Override
    public Iterable<UserRes> findAll(RestParamRequest paramRequest) {
        Specification<User> specification = createFilter(paramRequest);
        Iterable<User> listIterable = doFindAllFiltered(paramRequest, specification);
        if (listIterable instanceof Page<?>) {
            Page<User> page = (Page<User>) listIterable;
            List<UserRes> dtoList = page.getContent().stream()
                    .map(userMapper::toRes)
                    .toList();
            return new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
        } else {
            List<User> list = (List<User>) listIterable;
            return list.stream()
                    .map(userMapper::toRes)
                    .toList();
        }
    }

    @Override
    public UserRes findById(String id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ApplicationException(null, "User not found", HttpStatus.NOT_FOUND)
        );

        return userMapper.toRes(user);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserRes create(UserReq req) {
        Set<Role> roles = roleService.findAllByIds(req.getRoles());
        if(roles.size() != req.getRoles().size()) {
            throw new ApplicationException(null, "Role is missing 1 or more", HttpStatus.BAD_REQUEST);
        }

        UserCredential userCredential = UserCredential.builder()
                .email(req.getEmail())
                .password(req.getPassword() != null && !req.getPassword().isEmpty() ? passwordEncoder.encode(req.getPassword()) : passwordEncoder.encode("12345"))
                .roles(roles)
                .build();

        User user = userMapper.toEntity(req);
        authService.registerFromAdmin(user, userCredential);
        return userMapper.toRes(user);
    }

    @Override
    public UserRes update(UserReq req, String id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ApplicationException(null, "User not found", HttpStatus.NOT_FOUND)
        );

        if (!req.getEmail().isEmpty())
            user.getUserCredential().setEmail(req.getEmail());
        if (!req.getFullName().isEmpty())
            user.setFullName(req.getFullName());
        if (!req.getGender().name().isEmpty())
            user.setGender(req.getGender());
        if (req.getBirthDate() != null)
            user.setBirthDate(req.getBirthDate());
        if (!req.getMaritalStatus().name().isEmpty())
            user.setMaritalStatus(req.getMaritalStatus());
        if (req.getRoles() != null && req.getRoles().isEmpty()){
            Set<Role> roles = roleService.findAllByIds(req.getRoles());
            if(roles.size() != req.getRoles().size()) {
                throw new ApplicationException(null, "Role is missing 1 or more", HttpStatus.BAD_REQUEST);
            }
            user.getUserCredential().setRoles(roles);
        }
        if (req.getPassword() != null && !req.getPassword().isEmpty()) {
            user.getUserCredential().setPassword(passwordEncoder.encode(req.getPassword()));
        }
        userRepository.save(user);
        return userMapper.toRes(user);
    }

    @Override
    public void delete(String id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new ApplicationException(null, "User not found", HttpStatus.NOT_FOUND)
        );

        user.setIsActive(false);
        user.getUserCredential().setIsActive(false);
        userRepository.save(user);
    }

    @Override
    protected Specification<User> createPredicate(RestFilter filter) {
        System.out.println("filter: " + filter);
        return (root, query, cb) -> {
            Join<User, UserCredential> userCredentialJoin = root.join("userCredential", JoinType.INNER);
            return switch (filter.getPath()) {
                case "fullName" -> cb.equal(
                        cb.lower(root.get("fullName")),
                        filter.getValue().toString().toLowerCase()
                );
                case "gender" ->  cb.equal(
                        cb.lower(root.get("gender")),
                        filter.getValue().toString().toLowerCase()
                );
                case "birthDate" ->  cb.equal(
                        root.get("birthDate"),
                        Long.parseLong(filter.getValue().toString())
                );
                case "maritalStatus" ->  cb.equal(
                        cb.lower(root.get("maritalStatus")),
                        filter.getValue().toString().toLowerCase()
                );
                case "userCredential.email" ->  cb.equal(
                        cb.lower(userCredentialJoin.get("email")),
                        filter.getValue().toString().toLowerCase()
                );
                default -> null;
            };
        };
    }

    @Override
    protected Specification<User> createSearchPredicate(String search) {
        Long birthDate;
        try {
            birthDate = Long.parseLong(search);
        } catch (NumberFormatException e) {
            birthDate = 0L;
        }
        Long finalBirthDate = birthDate;
        return (root, query, cb) -> {
            Join<User, UserCredential> userCredentialJoin = root.join("userCredential", JoinType.INNER);
            return cb.or(
                cb.like(
                    cb.lower(root.get("fullName")),
                    "%" + search.toLowerCase() + "%"
                ),
                cb.like(
                    cb.lower(root.get("gender")),
                    "%" + search.toLowerCase() + "%"
                ),
                cb.equal(
                        root.get("birthDate"),
                        finalBirthDate
                ),
                cb.like(
                        cb.lower(root.get("maritalStatus")),
                        "%" + search.toLowerCase() + "%"
                ),
                cb.like(
                        cb.lower(userCredentialJoin.get("email")),
                        "%" + search.toLowerCase() + "%"
                )
            );
        };
    }

    @Override
    protected BaseRepository<User, String> getRepository() {
        return userRepository;
    }
}
