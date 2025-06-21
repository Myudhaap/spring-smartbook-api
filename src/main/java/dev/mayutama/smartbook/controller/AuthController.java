package dev.mayutama.smartbook.controller;

import dev.mayutama.smartbook.constant.AppPath;
import dev.mayutama.smartbook.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(AppPath.AUTH)
@RequiredArgsConstructor
public class AuthController {

    @PostMapping("/register")
    public ResponseEntity<?> register(){
        return ResponseUtil.responseSuccess(null, "Success Register");
    }
}
