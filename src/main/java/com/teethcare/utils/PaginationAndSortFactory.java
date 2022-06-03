package com.teethcare.utils;

import org.springframework.data.domain.*;

import java.util.List;

public class PaginationAndSortFactory {
    public static Pageable pagingAndSorting(int pageSize, int pageNo, String sortBy, String sortDirection) {

        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();

        return PageRequest.of(pageNo, pageSize, sort);
    }
    public static <T> Page<T> convertToPage(List<T> list, Pageable pageable) {
        Pageable paging = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        int start = Math.min((int) paging.getOffset(), list.size());
        int end = Math.min((start + paging.getPageSize()), list.size());
        return new PageImpl<>(list.subList(start, end), pageable, list.size());
    }
}
