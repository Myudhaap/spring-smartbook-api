package dev.mayutama.smartbook.model.dto.request.genre;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class GenreReq {
    @NotNull(message = "Field name is required")
    private String name;
    private String description;
}
