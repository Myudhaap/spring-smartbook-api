package dev.mayutama.smartbook.service;

import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.model.dto.request.book.BookReq;
import dev.mayutama.smartbook.model.dto.response.book.BookRes;

public interface BookService {
    Iterable<BookRes> findAll(RestParamRequest paramRequest);
    BookRes findById(String id);
    BookRes create(BookReq req);
    BookRes update(BookReq req, String id);
    void delete(String id);
}
