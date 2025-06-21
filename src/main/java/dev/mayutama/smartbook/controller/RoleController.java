package dev.mayutama.smartbook.controller;

import dev.mayutama.smartbook.constant.AppPath;
import dev.mayutama.smartbook.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppPath.ROLE)
@RequiredArgsConstructor
public class RoleController {

    @GetMapping
    public ResponseEntity<?> getAll(){
        return ResponseUtil.responseSuccess(null, "Success get all roles");
    }
}
