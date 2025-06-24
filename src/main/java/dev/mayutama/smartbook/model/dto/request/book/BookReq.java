package dev.mayutama.smartbook.model.dto.request.book;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class BookReq {
    @NotNull(message = "Field title is required")
    private String title;
    private String author;
    private String description;
    private Long publishedAt;
    @NotNull(message = "Field genres is required")
    @Size(min = 1, message = "Field genres must contain at least one item")
    private Set<String> genres;
}
