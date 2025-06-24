package dev.mayutama.smartbook.service;

import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.model.dto.request.review.ReviewReq;
import dev.mayutama.smartbook.model.dto.response.review.ReviewRes;

import java.util.List;

public interface ReviewService {
    Iterable<ReviewRes> findAll(RestParamRequest paramRequest);
    ReviewRes findById(String id);
    ReviewRes create(ReviewReq req);
    ReviewRes update(ReviewReq req, String id);
    void delete(String id);
    List<ReviewRes> findAllByBookId(String bookId);
    ReviewRes findByBookIdAndUserId(String bookId);
}
