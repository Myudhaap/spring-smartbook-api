package dev.mayutama.smartbook.common.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class CommonPagingResponse<T> {
    private Integer statusCode;
    private String message;
    private T data;
    private PagingResponse paging;
}
