package dev.mayutama.smartbook.service;

import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.model.dto.request.role.RoleReq;
import dev.mayutama.smartbook.model.dto.request.user.UserReq;
import dev.mayutama.smartbook.model.dto.response.role.RoleRes;
import dev.mayutama.smartbook.model.dto.response.user.UserRes;
import dev.mayutama.smartbook.model.entity.Role;
import dev.mayutama.smartbook.model.entity.User;

public interface UserService {
    User findEntityById(String id);
    UserRes findByUserCredentialId(String userCredentialId);
    Iterable<UserRes> findAll(RestParamRequest paramRequest);
    UserRes findById(String id);
    UserRes create(UserReq req);
    UserRes update(UserReq req, String id);
    void delete(String id);
}
