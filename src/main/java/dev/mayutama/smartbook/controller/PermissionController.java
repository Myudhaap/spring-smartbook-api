package dev.mayutama.smartbook.controller;

import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.constant.AppPath;
import dev.mayutama.smartbook.model.dto.request.permission.PermissionReq;
import dev.mayutama.smartbook.model.dto.response.permission.PermissionRes;
import dev.mayutama.smartbook.service.PermissionService;
import dev.mayutama.smartbook.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppPath.PERMISSION)
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping
    public ResponseEntity<?> getAll(
        @ModelAttribute RestParamRequest paramRequest
    ) {
        Iterable<PermissionRes> res = permissionService.findAll(paramRequest);
        if (res instanceof Page<?>) {
            return ResponseUtil.responseSuccessWithPaging((Page<?>) res, "Success get all permission");
        } else {
            return ResponseUtil.responseSuccess(res, "Success get all permission");
        }
    }

    @GetMapping(AppPath.BY_ID)
    public ResponseEntity<?> getById(
        @PathVariable String id
    ) {
        PermissionRes res = permissionService.findById(id);
        return ResponseUtil.responseSuccess(res, "Success get by id: " + id + " permission");
    }

    @PostMapping
    public ResponseEntity<?> create(
        @RequestBody @Valid PermissionReq req
    ) {
        PermissionRes res = permissionService.create(req);
        return ResponseUtil.responseSuccessCreate(res, "Success create permission");
    }

    @PutMapping(AppPath.BY_ID)
    public ResponseEntity<?> update(
        @RequestBody @Valid PermissionReq req,
        @PathVariable String id
    ) {
        PermissionRes res = permissionService.update(req, id);
        return ResponseUtil.responseSuccess(res, "Success update permission");
    }

    @DeleteMapping(AppPath.BY_ID)
    public ResponseEntity<?> delete(
        @PathVariable String id
    ) {
        permissionService.delete(id);
        return ResponseUtil.responseSuccess(null, "Success delete permission");
    }
}
