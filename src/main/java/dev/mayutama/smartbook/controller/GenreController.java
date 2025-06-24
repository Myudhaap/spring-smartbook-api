package dev.mayutama.smartbook.controller;

import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.constant.AppPath;
import dev.mayutama.smartbook.model.dto.request.genre.GenreReq;
import dev.mayutama.smartbook.model.dto.response.genre.GenreRes;
import dev.mayutama.smartbook.service.GenreService;
import dev.mayutama.smartbook.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppPath.GENRE)
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN') or hasAuthority('PERMIT_ALL')")
public class GenreController {
    private final GenreService genreService;


    @GetMapping
    public ResponseEntity<?> getAll(
            @ModelAttribute RestParamRequest paramRequest
    ) {
        Iterable<GenreRes> res = genreService.findAll(paramRequest);
        if (res instanceof Page<?>) {
            return ResponseUtil.responseSuccessWithPaging((Page<?>) res, "Success get all genre");
        } else {
            return ResponseUtil.responseSuccess(res, "Success get all genre");
        }
    }

    @GetMapping(AppPath.BY_ID)
    public ResponseEntity<?> getById(
            @PathVariable String id
    ) {
        GenreRes res = genreService.findById(id);
        return ResponseUtil.responseSuccess(res, "Success get by id: " + id + " genre");
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody @Valid GenreReq req
    ) {
        GenreRes res = genreService.create(req);
        return ResponseUtil.responseSuccessCreate(res, "Success create genre");
    }

    @PutMapping(AppPath.BY_ID)
    public ResponseEntity<?> update(
            @RequestBody @Valid GenreReq req,
            @PathVariable String id
    ) {
        GenreRes res = genreService.update(req, id);
        return ResponseUtil.responseSuccessCreate(res, "Success update genre");
    }

    @DeleteMapping(AppPath.BY_ID)
    public ResponseEntity<?> delete(
            @PathVariable String id
    ) {
        genreService.delete(id);
        return ResponseUtil.responseSuccessCreate(null, "Success delete genre");
    }
}
