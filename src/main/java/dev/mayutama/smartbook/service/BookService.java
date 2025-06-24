package dev.mayutama.smartbook.service;

import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.model.dto.request.book.BookReq;
import dev.mayutama.smartbook.model.dto.response.book.BookRes;
import dev.mayutama.smartbook.model.dto.response.dashboard.BookRatingSummaryRes;
import dev.mayutama.smartbook.model.entity.Book;

import java.util.List;

public interface BookService {
    Iterable<BookRes> findAll(RestParamRequest paramRequest);
    BookRes findById(String id);
    Book findEntityById(String id);
    BookRes create(BookReq req);
    BookRes update(BookReq req, String id);
    void delete(String id);
    List<BookRatingSummaryRes> getTopRatedBooks(Integer limit);
}
