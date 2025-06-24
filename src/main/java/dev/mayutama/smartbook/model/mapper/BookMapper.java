package dev.mayutama.smartbook.model.mapper;

import dev.mayutama.smartbook.model.dto.request.book.BookReq;
import dev.mayutama.smartbook.model.dto.response.book.BookRes;
import dev.mayutama.smartbook.model.dto.response.genre.GenreRes;
import dev.mayutama.smartbook.model.entity.Book;
import dev.mayutama.smartbook.model.entity.Genre;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookMapper {
    private final GenreMapper genreMapper;

    public Book toEntity(BookReq req, Set<Genre> genres) {
        return Book.builder()
                .title(req.getTitle())
                .author(req.getAuthor())
                .description(req.getDescription())
                .publishedAt(req.getPublishedAt())
                .genres(genres)
                .build();
    }

    public BookRes toRes(Book book) {
        Set<GenreRes> genres = book.getGenres().stream()
                .map(genreMapper::toRes)
                .collect(Collectors.toSet());

        return BookRes.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .description(book.getDescription())
                .publishedAt(book.getPublishedAt())
                .genres(genres)
                .createdBy(book.getCreatedBy())
                .createdAt(book.getCreatedAt())
                .updatedBy(book.getUpdatedBy())
                .updatedAt(book.getUpdatedAt())
                .build();
    }
}
