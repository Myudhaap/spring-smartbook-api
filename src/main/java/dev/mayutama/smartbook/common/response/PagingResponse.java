package dev.mayutama.smartbook.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PagingResponse {
    private Integer currentPage;
    private Integer size;
    private Integer totalPages;
    private Long totalElements;
}
