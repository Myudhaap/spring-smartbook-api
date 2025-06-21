package dev.mayutama.smartbook.util;

import ch.qos.logback.core.util.StringUtil;
import dev.mayutama.smartbook.common.response.CommonPagingResponse;
import dev.mayutama.smartbook.common.response.CommonResponse;
import dev.mayutama.smartbook.common.response.PagingResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    public static ResponseEntity<?> responseSuccessCreate(
            Object obj,
            String message
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.<Object>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message(StringUtil.isNullOrEmpty(message) ? "Success created" : message)
                        .data(obj)
                        .build()
                );
    }

    public static ResponseEntity<?> responseSuccess(
            Object obj,
            String message
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonResponse.<Object>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(StringUtil.isNullOrEmpty(message) ? "Success" : message)
                        .data(obj)
                        .build()
                );
    }

    public static ResponseEntity<?> responseSuccessWithPaging(
            Page<?> obj,
            String message
    ) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(CommonPagingResponse.<Object>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message(StringUtil.isNullOrEmpty(message) ? "Success" : message)
                        .data(obj.getContent())
                        .paging(PagingResponse.builder()
                                .currentPage(obj.getNumber() + 1)
                                .size(obj.getSize())
                                .totalPages(obj.getTotalPages())
                                .totalElements(obj.getTotalElements())
                                .build()
                        )
                        .build()
                );
    }
}
