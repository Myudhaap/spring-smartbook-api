package dev.mayutama.smartbook.service.impl;

import dev.mayutama.smartbook.common.repository.BaseRepository;
import dev.mayutama.smartbook.common.rest.RestFilter;
import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.common.service.EngineFilterService;
import dev.mayutama.smartbook.exception.ApplicationException;
import dev.mayutama.smartbook.model.dto.request.permission.PermissionReq;
import dev.mayutama.smartbook.model.dto.response.permission.PermissionRes;
import dev.mayutama.smartbook.model.entity.Permission;
import dev.mayutama.smartbook.model.mapper.PermissionMapper;
import dev.mayutama.smartbook.repository.PermissionRepository;
import dev.mayutama.smartbook.service.PermissionService;
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
public class PermissionServiceImpl
    extends EngineFilterService<Permission, String>
    implements PermissionService
{
    private final PermissionMapper permissionMapper;

    private final PermissionRepository permissionRepository;

    @Override
    public Iterable<PermissionRes> findAll(RestParamRequest paramRequest) {
        Specification<Permission> specification = createFilter(paramRequest);
        Iterable<Permission> listIterable = doFindAllFiltered(paramRequest, specification);
        if (listIterable instanceof Page<?>) {
            Page<Permission> page = (Page<Permission>) listIterable;
            List<PermissionRes> dtoList = page.getContent().stream()
                    .map(permissionMapper::toRes)
                    .toList();
            return new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
        } else {
            List<Permission> list = (List<Permission>) listIterable;
            return list.stream()
                    .map(permissionMapper::toRes)
                    .toList();
        }
    }

    @Override
    public PermissionRes findById(String id) {
        Permission res = permissionRepository.findByIdIsActive(id).orElseThrow(
                () -> new ApplicationException(null, "Permission not found", HttpStatus.NOT_FOUND)
        );

        return permissionMapper.toRes(res);
    }

    @Override
    public PermissionRes create(PermissionReq req) {
        Permission permission = permissionMapper.toEntity(req);
        permissionRepository.save(permission);
        return permissionMapper.toRes(permission);
    }

    @Override
    public PermissionRes update(PermissionReq req, String id) {
        Permission res = permissionRepository.findByIdIsActive(id).orElseThrow(
                () -> new ApplicationException(null, "Permission not found", HttpStatus.NOT_FOUND)
        );

        if (!req.getName().isEmpty())
            res.setName(req.getName());

        permissionRepository.save(res);
        return permissionMapper.toRes(res);
    }

    @Override
    public void delete(String id) {
        Permission res = permissionRepository.findByIdIsActive(id).orElseThrow(
                () -> new ApplicationException(null, "Permission not found", HttpStatus.NOT_FOUND)
        );

        res.setIsActive(false);
        permissionRepository.save(res);
    }

    @Override
    protected Specification<Permission> createPredicate(RestFilter filter) {
        return switch (filter.getPath()) {
            case "name" -> (root, query, cb) -> cb.like(
                    cb.lower(root.get("name")),
                    "%" + filter.getValue().toString().toLowerCase() + "%"
            );
            default -> null;
        };
    }

    @Override
    protected Specification<Permission> createSearchPredicate(String search) {
        return (root, query, cb) -> cb.or(
                cb.like(
                        cb.lower(root.get("name")),
                        "%" + search.toLowerCase() + "%"
                )
        );
    }

    @Override
    public Set<Permission> findAllByIds(Set<String> ids) {
        return permissionRepository.findAllByIdIn(ids);
    }

    @Override
    protected BaseRepository<Permission, String> getRepository() {
        return permissionRepository;
    }
}
