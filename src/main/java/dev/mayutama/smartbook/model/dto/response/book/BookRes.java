package dev.mayutama.smartbook.model.dto.response.book;

import dev.mayutama.smartbook.common.response.BaseResponse;
import dev.mayutama.smartbook.model.dto.response.genre.GenreRes;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class BookRes extends BaseResponse {
    private String id;
    private String title;
    private String author;
    private String description;
    private Long publishedAt;
    private Set<GenreRes> genres;
}
