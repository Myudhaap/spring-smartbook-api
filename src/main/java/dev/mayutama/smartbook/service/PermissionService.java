package dev.mayutama.smartbook.service;

import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.model.dto.request.permission.PermissionReq;
import dev.mayutama.smartbook.model.dto.response.permission.PermissionRes;

import java.util.List;

public interface PermissionService {
    Iterable<PermissionRes> findAll(RestParamRequest paramRequest);
    PermissionRes findById(String id);
    PermissionRes create(PermissionReq req);
    PermissionRes update(PermissionReq req, String id);
    void delete(String id);
}
