package dev.mayutama.smartbook.model.mapper;

import dev.mayutama.smartbook.model.dto.request.genre.GenreReq;
import dev.mayutama.smartbook.model.dto.response.genre.GenreRes;
import dev.mayutama.smartbook.model.entity.Genre;
import org.springframework.stereotype.Component;

@Component
public class GenreMapper {
    public Genre toEntity(GenreReq req) {
        return Genre.builder()
                .name(req.getName())
                .description(req.getDescription())
                .build();
    }

    public GenreRes toRes(Genre genre){
        return GenreRes.builder()
                .id(genre.getId())
                .name(genre.getName())
                .description(genre.getDescription())
                .createdBy(genre.getCreatedBy())
                .createdAt(genre.getCreatedAt())
                .updatedBy(genre.getUpdatedBy())
                .updatedAt(genre.getUpdatedAt())
                .build();
    }
}
