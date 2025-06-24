package dev.mayutama.smartbook.service.impl;

import dev.mayutama.smartbook.common.repository.BaseRepository;
import dev.mayutama.smartbook.common.rest.RestFilter;
import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.common.service.EngineFilterService;
import dev.mayutama.smartbook.exception.ApplicationException;
import dev.mayutama.smartbook.model.dto.request.genre.GenreReq;
import dev.mayutama.smartbook.model.dto.response.genre.GenreRes;
import dev.mayutama.smartbook.model.entity.Genre;
import dev.mayutama.smartbook.model.mapper.GenreMapper;
import dev.mayutama.smartbook.repository.GenreRepository;
import dev.mayutama.smartbook.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl
    extends EngineFilterService<Genre, String>
    implements GenreService
{
    private final GenreRepository genreRepository;
    private final GenreMapper genreMapper;

    @Override
    public Iterable<GenreRes> findAll(RestParamRequest paramRequest) {
        Specification<Genre> specification = createFilter(paramRequest);
        Iterable<Genre> listIterable = doFindAllFiltered(paramRequest, specification);
        if (listIterable instanceof Page<?>) {
            Page<Genre> page = (Page<Genre>) listIterable;
            List<GenreRes> dtoList = page.getContent().stream()
                    .map(genreMapper::toRes)
                    .toList();
            return new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
        } else {
            List<Genre> list = (List<Genre>) listIterable;
            return list.stream()
                    .map(genreMapper::toRes)
                    .toList();
        }
    }

    @Override
    public GenreRes findById(String id) {
        Genre genre = genreRepository.findByIdActive(id).orElseThrow(
            () -> new ApplicationException(null, "Genre not found", HttpStatus.NOT_FOUND)
        );

        return genreMapper.toRes(genre);
    }

    @Override
    public GenreRes create(GenreReq req) {
        Genre genre = genreMapper.toEntity(req);
        genreRepository.save(genre);
        return genreMapper.toRes(genre);
    }

    @Override
    public GenreRes update(GenreReq req, String id) {
        Genre genre = genreRepository.findByIdActive(id).orElseThrow(
                () -> new ApplicationException(null, "Genre not found", HttpStatus.NOT_FOUND)
        );

        if (!req.getName().isEmpty())
            genre.setName(req.getName());

        if (req.getDescription() != null && !req.getDescription().isEmpty())
            genre.setDescription(req.getDescription());

        genreRepository.save(genre);
        return genreMapper.toRes(genre);
    }

    @Override
    public void delete(String id) {
        Genre genre = genreRepository.findByIdActive(id).orElseThrow(
                () -> new ApplicationException(null, "Genre not found", HttpStatus.NOT_FOUND)
        );

        genre.setIsActive(false);
        genreRepository.save(genre);
    }

    @Override
    public Set<Genre> findAllByIds(Set<String> ids) {
        return genreRepository.findAllByIdIn(ids);
    }

    @Override
    protected Specification<Genre> createPredicate(RestFilter filter) {
        return (root, query, cb) -> {
            return switch (filter.getPath()) {
                case "name" -> cb.like(
                        cb.lower(root.get("name")),
                        "%" + filter.getValue().toString().toLowerCase() + "%"
                );
                case "description" ->  cb.like(
                        cb.lower(root.get("description")),
                        "%" + filter.getValue().toString().toLowerCase() + "%"
                );
                default -> null;
            };
        };
    }

    @Override
    protected Specification<Genre> createSearchPredicate(String search) {
        return (root, query, cb) -> cb.or(
                cb.like(
                        cb.lower(root.get("name")),
                        "%" + search.toLowerCase() + "%"
                ),
                cb.like(
                        cb.lower(root.get("description")),
                        "%" + search.toLowerCase() + "%"
                )
        );
    }

    @Override
    protected BaseRepository<Genre, String> getRepository() {
        return genreRepository;
    }
}
