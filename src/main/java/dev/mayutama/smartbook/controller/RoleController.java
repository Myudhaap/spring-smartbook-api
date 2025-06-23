package dev.mayutama.smartbook.controller;

import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.constant.AppPath;
import dev.mayutama.smartbook.model.dto.request.role.RoleReq;
import dev.mayutama.smartbook.model.dto.response.role.RoleRes;
import dev.mayutama.smartbook.service.RoleService;
import dev.mayutama.smartbook.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppPath.ROLE)
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @ModelAttribute RestParamRequest paramRequest
    ) {
        Iterable<RoleRes> res = roleService.findAll(paramRequest);
        if (res instanceof Page<?>) {
            return ResponseUtil.responseSuccessWithPaging((Page<?>) res, "Success get all role");
        } else {
            return ResponseUtil.responseSuccess(res, "Success get all role");
        }
    }

    @GetMapping(AppPath.BY_ID)
    public ResponseEntity<?> getById(
            @PathVariable String id
    ) {
        RoleRes res = roleService.findById(id);
        return ResponseUtil.responseSuccess(res, "Success get by id: " + id + " role");
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody @Valid RoleReq req
    ) {
        RoleRes res = roleService.create(req);
        return ResponseUtil.responseSuccessCreate(res, "Success create role");
    }

    @PutMapping(AppPath.BY_ID)
    public ResponseEntity<?> update(
            @RequestBody @Valid RoleReq req,
            @PathVariable String id
    ) {
        RoleRes res = roleService.update(req, id);
        return ResponseUtil.responseSuccessCreate(res, "Success update role");
    }

    @DeleteMapping(AppPath.BY_ID)
    public ResponseEntity<?> delete(
            @PathVariable String id
    ) {
        roleService.delete(id);
        return ResponseUtil.responseSuccessCreate(null, "Success delete role");
    }
}
