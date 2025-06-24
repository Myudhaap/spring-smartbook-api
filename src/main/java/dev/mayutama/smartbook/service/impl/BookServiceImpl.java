package dev.mayutama.smartbook.service.impl;

import dev.mayutama.smartbook.common.repository.BaseRepository;
import dev.mayutama.smartbook.common.rest.RestFilter;
import dev.mayutama.smartbook.common.rest.RestParamRequest;
import dev.mayutama.smartbook.common.service.EngineFilterService;
import dev.mayutama.smartbook.exception.ApplicationException;
import dev.mayutama.smartbook.model.dto.request.book.BookReq;
import dev.mayutama.smartbook.model.dto.response.book.BookRes;
import dev.mayutama.smartbook.model.dto.response.genre.GenreRes;
import dev.mayutama.smartbook.model.entity.Book;
import dev.mayutama.smartbook.model.entity.Genre;
import dev.mayutama.smartbook.model.entity.Permission;
import dev.mayutama.smartbook.model.entity.Role;
import dev.mayutama.smartbook.model.mapper.BookMapper;
import dev.mayutama.smartbook.repository.BookRepository;
import dev.mayutama.smartbook.service.BookService;
import dev.mayutama.smartbook.service.GenreService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
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
public class BookServiceImpl
    extends EngineFilterService<Book, String>
    implements BookService
{
    private final BookRepository bookRepository;
    private final GenreService genreService;
    private final BookMapper bookMapper;

    @Override
    public Iterable<BookRes> findAll(RestParamRequest paramRequest) {
        Specification<Book> specification = createFilter(paramRequest);
        Iterable<Book> listIterable = doFindAllFiltered(paramRequest, specification);
        if (listIterable instanceof Page<?>) {
            Page<Book> page = (Page<Book>) listIterable;
            List<BookRes> dtoList = page.getContent().stream()
                    .map(bookMapper::toRes)
                    .toList();
            return new PageImpl<>(dtoList, page.getPageable(), page.getTotalElements());
        } else {
            List<Book> list = (List<Book>) listIterable;
            return list.stream()
                    .map(bookMapper::toRes)
                    .toList();
        }
    }

    @Override
    public BookRes findById(String id) {
        System.out.println("id: " + id);
        Book book = bookRepository.findById(id).orElseThrow(
            () -> new ApplicationException(null, "Book not found", HttpStatus.NOT_FOUND)
        );

        if (!book.getIsActive())
            throw new ApplicationException(null, "Book not found", HttpStatus.NOT_FOUND);

        System.out.println(book);

        return bookMapper.toRes(book);
    }

    @Override
    public BookRes create(BookReq req) {
        Set<Genre> genres = genreService.findAllByIds(req.getGenres());
        if (genres.size() != req.getGenres().size()) {
            throw new ApplicationException(null, "Genre is missing 1 or more", HttpStatus.BAD_REQUEST);
        }

        Book book = bookMapper.toEntity(req, genres);
        bookRepository.save(book);
        return bookMapper.toRes(book);
    }

    @Override
    public BookRes update(BookReq req, String id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new ApplicationException(null, "Book not found", HttpStatus.NOT_FOUND)
        );

        if (!book.getIsActive())
            throw new ApplicationException(null, "Book not found", HttpStatus.NOT_FOUND);

        Set<Genre> genres = genreService.findAllByIds(req.getGenres());
        if (genres.size() != req.getGenres().size()) {
            throw new ApplicationException(null, "Genre is missing 1 or more", HttpStatus.BAD_REQUEST);
        }

        if (!req.getTitle().isEmpty())
            book.setTitle(req.getTitle());
        if (req.getAuthor() != null && !req.getAuthor().isEmpty())
            book.setAuthor(req.getAuthor());
        if (req.getDescription() != null && !req.getDescription().isEmpty())
            book.setDescription(req.getDescription());
        if (req.getPublishedAt() != null)
            book.setPublishedAt(req.getPublishedAt());
        book.setGenres(genres);

        bookRepository.save(book);

        return bookMapper.toRes(book);
    }

    @Override
    public void delete(String id) {
        Book book = bookRepository.findById(id).orElseThrow(
                () -> new ApplicationException(null, "Book not found", HttpStatus.NOT_FOUND)
        );

        if (!book.getIsActive())
            throw new ApplicationException(null, "Book not found", HttpStatus.NOT_FOUND);

        book.setIsActive(false);
        bookRepository.save(book);
    }

    @Override
    protected Specification<Book> createPredicate(RestFilter filter) {
        return (root, query, cb) -> {
            Join<Book, Genre> genreJoin = root.join("genres", JoinType.INNER);
            return switch (filter.getPath()) {
                case "title" -> cb.equal(
                        cb.lower(root.get("title")),
                        filter.getValue().toString().toLowerCase()
                );
                case "author" -> cb.equal(
                        cb.lower(root.get("author")),
                        filter.getValue().toString().toLowerCase()
                );
                case "publishedAt" -> cb.equal(
                        root.get("publishedAt"),
                        Long.parseLong(filter.getValue().toString())
                );
                case "genres.name" ->  cb.equal(
                        cb.lower(genreJoin.get("name")),
                        filter.getValue().toString().toLowerCase()
                );
                case "genres.description" ->  cb.equal(
                        cb.lower(genreJoin.get("description")),
                        filter.getValue().toString().toLowerCase()
                );
                default -> null;
            };
        };
    }

    @Override
    protected Specification<Book> createSearchPredicate(String search) {
        Long publishedAt;
        try {
            publishedAt = Long.parseLong(search);
        } catch (NumberFormatException e) {
            publishedAt = 0L;
        }
        Long finalPublishedAt = publishedAt;
        return (root, query, cb) -> {
            Join<Book, Genre> genreJoin = root.join("genres", JoinType.INNER);
            return cb.or(
                    cb.like(
                            cb.lower(root.get("title")),
                            "%" + search.toLowerCase() + "%"
                    ),
                    cb.like(
                            cb.lower(root.get("author")),
                            "%" + search.toLowerCase() + "%"
                    ),
                    cb.equal(
                            root.get("publishedAt"),
                            finalPublishedAt
                    ),
                    cb.like(
                            cb.lower(genreJoin.get("name")),
                            "%" + search.toLowerCase() + "%"
                    ),
                    cb.like(
                            cb.lower(genreJoin.get("description")),
                            "%" + search.toLowerCase() + "%"
                    )
            );
        };
    }

    @Override
    protected BaseRepository<Book, String> getRepository() {
        return bookRepository;
    }
}
