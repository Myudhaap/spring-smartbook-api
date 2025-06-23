package dev.mayutama.smartbook.common.rest;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Set;

@Data
@NoArgsConstructor
public class RestParamRequest {
    private Set<String> expand;
    private Set<String> include;
    private Set<String> exclude;

    private String search;
    private Map<String, String[]> filter;

    private Map<String, String> param;

    private Set<String> sort;

    private Integer page;
    private Integer size = 25;

    private Integer offset;
    private Integer limit = 25;

    public Integer getSizeOrLimit()
    {
        return size != null ? size : limit;
    }

    public Integer getLimitOrSize()
    {
        return limit != null ? limit : size;
    }

    public boolean isPageRequest()
    {
        return page != null && getSizeOrLimit() != null;
    }

    public boolean isSearchRequest()
    {
        return search != null;
    }

    public boolean isOffsetLimitRequest()
    {
        return offset != null && getLimitOrSize() != null;
    }
}
