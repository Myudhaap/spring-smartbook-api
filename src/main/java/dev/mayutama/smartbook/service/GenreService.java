package dev.mayutama.smartbook.service;

import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.model.dto.request.genre.GenreReq;
import dev.mayutama.smartbook.model.dto.response.genre.GenreRes;
import dev.mayutama.smartbook.model.entity.Genre;

import java.util.Set;

public interface GenreService {
    Iterable<GenreRes> findAll(RestParamRequest paramRequest);
    GenreRes findById(String id);
    GenreRes create(GenreReq req);
    GenreRes update(GenreReq req, String id);
    void delete(String id);
    Set<Genre> findAllByIds(Set<String> ids);
}
