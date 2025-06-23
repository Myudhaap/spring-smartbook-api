package dev.mayutama.smartbook.service;

import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.model.dto.request.permission.PermissionReq;
import dev.mayutama.smartbook.model.dto.request.role.RoleReq;
import dev.mayutama.smartbook.model.dto.response.permission.PermissionRes;
import dev.mayutama.smartbook.model.dto.response.role.RoleRes;

public interface RoleService {
    Iterable<RoleRes> findAll(RestParamRequest paramRequest);
    RoleRes findById(String id);
    RoleRes create(RoleReq req);
    RoleRes update(RoleReq req, String id);
    void delete(String id);
}
