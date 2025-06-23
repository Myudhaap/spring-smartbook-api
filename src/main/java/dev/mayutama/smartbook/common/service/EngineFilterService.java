package dev.mayutama.smartbook.common.service;

import dev.mayutama.smartbook.common.repository.BaseRepository;
import dev.mayutama.smartbook.common.rest.RestEngine;
import dev.mayutama.smartbook.common.rest.RestFilter;
import dev.mayutama.smartbook.common.rest.RestParamRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.util.Set;

public abstract class EngineFilterService<T, ID extends Serializable> {
    protected abstract Specification<T> createPredicate(RestFilter filter);
    protected abstract Specification<T> createSearchPredicate(String search);
    protected abstract BaseRepository<T, ID> getRepository();

    protected RestEngine restEngine = new RestEngine();
    @SuppressWarnings("unchecked")
    private final Class<T> resourceClass = (Class<T>) ((ParameterizedType) getClass()
            .getGenericSuperclass())
            .getActualTypeArguments()[0];

    public Specification<T> createFilter(RestParamRequest paramRequest) {
        Specification<T> spec = (root, query, cb) -> cb.conjunction();

        // always get data is_active is true
        try {
            spec = spec.and(
                    (root, query, cb) -> cb.equal(
                            root.get("isActive"), true
                    )
            );
        }catch (Exception e){
            e.printStackTrace();
        }

        try {
            for (RestFilter restFilter : processFilter(paramRequest)) {
                Specification<T> currSpec = createPredicate(restFilter);

                if (currSpec != null) {
                    spec = spec.and(currSpec);
                }
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        if(paramRequest.isSearchRequest()) {
                spec = spec.and(createSearchPredicate(paramRequest.getSearch()));
        }

        return spec;
    }

    public Set<RestFilter> processFilter(RestParamRequest paramRequest) throws ReflectiveOperationException, ParseException {
        return restEngine.processFilter(resourceClass, paramRequest.getFilter());
    }

    public Iterable<T> doFindAllFiltered(RestParamRequest paramRequest, Specification<T> specification)
    {
        Sort sort = toSort(paramRequest);
        Pageable page = toPageable(paramRequest, sort);
        if(page != null)
            return getRepository().findAll(specification, page);
        else if(sort != null)
            return getRepository().findAll(specification, sort);
        else
            return getRepository().findAll(specification);
    }

    public Sort toSort(RestParamRequest paramRequest)
    {
        Sort sort = null;

        if(paramRequest.getSort() != null)
        {
            for(String sortBy : paramRequest.getSort())
            {
                if(StringUtils.isBlank(sortBy))
                    continue;

                boolean sortByDesc = sortBy.startsWith("-");
                String sortByPath = sortByDesc ? sortBy.substring(1) : sortBy;
                Sort curSort = Sort.by(sortByPath);
                if(sortByDesc)
                    curSort = curSort.descending();
                else
                    curSort = curSort.ascending();

                if(sort == null)
                    sort = curSort;
                else
                    sort = sort.and(curSort);
            }
        }

        if(sort == null)
            sort = getDefaultSort();

        return sort;
    }

    public Sort getDefaultSort()
    {
        return null;
    }

    public Pageable toPageable(RestParamRequest paramRequest, Sort sort)
    {
        if(!paramRequest.isPageRequest())
            return null;

        if(paramRequest.getPage() <= 0)
            paramRequest.setPage(1);

        if(sort != null)
            return PageRequest.of(paramRequest.getPage() - 1, paramRequest.getSizeOrLimit(), sort);
        else
            return PageRequest.of(paramRequest.getPage() - 1, paramRequest.getSizeOrLimit());
    }
}
