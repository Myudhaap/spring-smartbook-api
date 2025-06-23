package dev.mayutama.smartbook.service.impl;

import dev.mayutama.smartbook.common.repository.BaseRepository;
import dev.mayutama.smartbook.common.rest.RestFilter;
import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.common.service.EngineFilterService;
import dev.mayutama.smartbook.exception.ApplicationException;
import dev.mayutama.smartbook.model.dto.request.role.RoleReq;
import dev.mayutama.smartbook.model.dto.response.permission.PermissionRes;
import dev.mayutama.smartbook.model.dto.response.role.RoleRes;
import dev.mayutama.smartbook.model.entity.Permission;
import dev.mayutama.smartbook.model.entity.Role;
import dev.mayutama.smartbook.model.mapper.RoleMapper;
import dev.mayutama.smartbook.repository.RoleRepository;
import dev.mayutama.smartbook.service.PermissionService;
import dev.mayutama.smartbook.service.RoleService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl
    extends EngineFilterService<Role, String>
    implements RoleService
{
    private final RoleMapper roleMapper;
    private final PermissionService permissionService;

    private final RoleRepository roleRepository;

    @Override
    public Iterable<RoleRes> findAll(RestParamRequest paramRequest) {
        Specification<Role> specification = createFilter(paramRequest);
        Iterable<Role> listIterable = doFindAllFiltered(paramRequest, specification);
        if (listIterable instanceof Page<?>) {
            Page<Role> page = (Page<Role>) listIterable;
            List<RoleRes> dtoList = page.getContent().stream()
                    .map(roleMapper::toRes)
                    .toList();
            return new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
        } else {
            List<Role> list = (List<Role>) listIterable;
            return list.stream()
                    .map(roleMapper::toRes)
                    .toList();
        }
    }

    @Override
    public RoleRes findById(String id) {
        Role role = roleRepository.findById(id).orElseThrow(
                () -> new ApplicationException(null, "Role not found", HttpStatus.NOT_FOUND)
        );

        return roleMapper.toRes(role);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public RoleRes create(RoleReq req) {
        Set<Permission> permissions = permissionService.findAllByIds(req.getPermissions());
        if (permissions.size() != req.getPermissions().size())
            throw new ApplicationException(null, "Permissions is missing 1 or more",  HttpStatus.BAD_REQUEST);

        Role role = roleMapper.toEntity(req, permissions);
        roleRepository.save(role);

        return roleMapper.toRes(role);
    }

    @Override
    public RoleRes update(RoleReq req, String id) {
        Role role = roleRepository.findById(id).orElseThrow(
                () -> new ApplicationException(null, "Role not found", HttpStatus.NOT_FOUND)
        );

        Set<Permission> permissions = permissionService.findAllByIds(req.getPermissions());
        if (permissions.size() != req.getPermissions().size())
            throw new ApplicationException(null, "Permissions is missing 1 or more",  HttpStatus.BAD_REQUEST);

        role.setPermissions(permissions);
        roleRepository.save(role);
        return roleMapper.toRes(role);
    }

    @Override
    public void delete(String id) {
        Role role = roleRepository.findById(id).orElseThrow(
                () -> new ApplicationException(null, "Role not found", HttpStatus.NOT_FOUND)
        );

        role.setIsActive(false);
        roleRepository.save(role);
    }

    @Override
    protected Specification<Role> createPredicate(RestFilter filter) {
        System.out.println("filter: " + filter);
        return (root, query, cb) -> {
            Join<Role, Permission> permissionJoin = root.join("permissions", JoinType.INNER);
            return switch (filter.getPath()) {
                case "name" -> cb.like(
                        cb.lower(root.get("name")),
                        "%" + filter.getValue().toString().toLowerCase() + "%"
                );
                case "role.name" ->  cb.like(
                        cb.lower(permissionJoin.get("name")),
                        "%" + filter.getValue().toString().toLowerCase() + "%"
                );
                default -> null;
            };
        };
    }

    @Override
    protected Specification<Role> createSearchPredicate(String search) {
        return (root, query, cb) -> cb.or(
                cb.like(
                        cb.lower(root.get("name")),
                        "%" + search.toLowerCase() + "%"
                )
        );
    }

    @Override
    protected BaseRepository<Role, String> getRepository() {
        return roleRepository;
    }
}
