package dev.mayutama.smartbook.controller;

import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.constant.AppPath;
import dev.mayutama.smartbook.model.dto.request.book.BookReq;
import dev.mayutama.smartbook.model.dto.response.book.BookRes;
import dev.mayutama.smartbook.service.BookService;
import dev.mayutama.smartbook.util.ResponseUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AppPath.BOOK)
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;


    @GetMapping
    public ResponseEntity<?> getAll(
            @ModelAttribute RestParamRequest paramRequest
    ) {
        Iterable<BookRes> res = bookService.findAll(paramRequest);
        if (res instanceof Page<?>) {
            return ResponseUtil.responseSuccessWithPaging((Page<?>) res, "Success get all book");
        } else {
            return ResponseUtil.responseSuccess(res, "Success get all book");
        }
    }

    @GetMapping(AppPath.BY_ID)
    public ResponseEntity<?> getById(
            @PathVariable String id
    ) {
        BookRes res = bookService.findById(id);
        return ResponseUtil.responseSuccess(res, "Success get by id: " + id + " book");
    }

    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody @Valid BookReq req
    ) {
        BookRes res = bookService.create(req);
        return ResponseUtil.responseSuccessCreate(res, "Success create book");
    }

    @PutMapping(AppPath.BY_ID)
    public ResponseEntity<?> update(
            @RequestBody @Valid BookReq req,
            @PathVariable String id
    ) {
        BookRes res = bookService.update(req, id);
        return ResponseUtil.responseSuccessCreate(res, "Success update book");
    }

    @DeleteMapping(AppPath.BY_ID)
    public ResponseEntity<?> delete(
            @PathVariable String id
    ) {
        bookService.delete(id);
        return ResponseUtil.responseSuccessCreate(null, "Success delete book");
    }
}
