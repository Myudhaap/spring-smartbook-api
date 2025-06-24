package dev.mayutama.smartbook.model.dto.response.genre;

import dev.mayutama.smartbook.common.response.BaseResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = false)
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class GenreRes extends BaseResponse {
    private String id;
    private String name;
    private String description;
}
