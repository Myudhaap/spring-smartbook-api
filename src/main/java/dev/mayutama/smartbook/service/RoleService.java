package dev.mayutama.smartbook.service;

import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.model.dto.request.role.RoleReq;
import dev.mayutama.smartbook.model.dto.response.role.RoleRes;
import dev.mayutama.smartbook.model.entity.Role;

import java.util.Set;

public interface RoleService {
    Iterable<RoleRes> findAll(RestParamRequest paramRequest);
    RoleRes findById(String id);
    RoleRes create(RoleReq req);
    RoleRes update(RoleReq req, String id);
    void delete(String id);
    Role finByName(String name);
    Set<Role> findAllByIds(Set<String> ids);
}
