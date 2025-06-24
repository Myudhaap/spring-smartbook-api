package dev.mayutama.smartbook.controller;

import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.constant.AppPath;
import dev.mayutama.smartbook.model.dto.request.review.ReviewReq;
import dev.mayutama.smartbook.model.dto.response.review.ReviewRes;
import dev.mayutama.smartbook.service.ReviewService;
import dev.mayutama.smartbook.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(AppPath.REVIEW)
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PreAuthorize("hasRole('ROLE_ADMIN') or hasAuthority('PERMIT_ALL')")
    @GetMapping
    public ResponseEntity<?> getAll(
            @ModelAttribute RestParamRequest paramRequest
    ) {
        Iterable<ReviewRes> res = reviewService.findAll(paramRequest);
        if (res instanceof Page<?>) {
            return ResponseUtil.responseSuccessWithPaging((Page<?>) res, "Success get all review");
        } else {
            return ResponseUtil.responseSuccess(res, "Success get all review");
        }
    }

    @GetMapping(AppPath.BY_ID)
    public ResponseEntity<?> getById(
            @PathVariable String id
    ) {
        ReviewRes res = reviewService.findById(id);
        return ResponseUtil.responseSuccess(res, "Success get by id: " + id + " review");
    }


    @GetMapping("/book" + AppPath.BY_ID )
    public ResponseEntity<?> getAllByBookId(
        @PathVariable String id
    ) {
        List<ReviewRes> res = reviewService.findAllByBookId(id);
        return ResponseUtil.responseSuccess(res, "Success get all by book id: " + id);
    }


    @GetMapping("/book" + AppPath.BY_ID + "/current" )
    public ResponseEntity<?> getByBookId(
            @PathVariable String id
    ) {
        ReviewRes res = reviewService.findByBookIdAndUserId(id);
        return ResponseUtil.responseSuccess(res, "Success get by book id: " + id);
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody @Valid ReviewReq req
    ) {
        ReviewRes res = reviewService.create(req);
        return ResponseUtil.responseSuccessCreate(res, "Success create review");
    }

    @PutMapping(AppPath.BY_ID)
    public ResponseEntity<?> update(
            @RequestBody @Valid ReviewReq req,
            @PathVariable String id
    ) {
        ReviewRes res = reviewService.update(req, id);
        return ResponseUtil.responseSuccessCreate(res, "Success update review");
    }

    @DeleteMapping(AppPath.BY_ID)
    public ResponseEntity<?> delete(
            @PathVariable String id
    ) {
        reviewService.delete(id);
        return ResponseUtil.responseSuccessCreate(null, "Success delete review");
    }
}
