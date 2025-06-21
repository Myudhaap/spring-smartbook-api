package dev.mayutama.smartbook.common.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(toBuilder = true)
public class CommonErrorResponse {
    private final String errorCode;
    private final Object message;
    private final Integer statusCode;
    private final String statusName;
    private final String path;
    private final String method;
}
