package dev.mayutama.smartbook.controller;

import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.constant.AppPath;
import dev.mayutama.smartbook.model.dto.request.user.UserReq;
import dev.mayutama.smartbook.model.dto.response.user.UserRes;
import dev.mayutama.smartbook.service.UserService;
import dev.mayutama.smartbook.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(AppPath.USER)
public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @ModelAttribute RestParamRequest paramRequest
    ) {
        Iterable<UserRes> res = userService.findAll(paramRequest);
        if (res instanceof Page<?>) {
            return ResponseUtil.responseSuccessWithPaging((Page<?>) res, "Success get all user");
        } else {
            return ResponseUtil.responseSuccess(res, "Success get all user");
        }
    }

    @GetMapping(AppPath.BY_ID)
    public ResponseEntity<?> getById(
            @PathVariable String id
    ) {
        UserRes res = userService.findById(id);
        return ResponseUtil.responseSuccess(res, "Success get by id: " + id + " user");
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody @Valid UserReq req
    ) {
        UserRes res = userService.create(req);
        return ResponseUtil.responseSuccessCreate(res, "Success create user");
    }

    @PutMapping(AppPath.BY_ID)
    public ResponseEntity<?> update(
            @RequestBody @Valid UserReq req,
            @PathVariable String id
    ) {
        UserRes res = userService.update(req, id);
        return ResponseUtil.responseSuccessCreate(res, "Success update user");
    }

    @DeleteMapping(AppPath.BY_ID)
    public ResponseEntity<?> delete(
            @PathVariable String id
    ) {
        userService.delete(id);
        return ResponseUtil.responseSuccessCreate(null, "Success delete user");
    }
}
