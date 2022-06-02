package com.teethcare.specification.builder;

import com.teethcare.model.entity.Booking;
import com.teethcare.specification.BookingSpecification;
import com.teethcare.specification.SearchCriteria;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BookingBuilder {
    private final List<SearchCriteria> params;

    public BookingBuilder() {
        params = new ArrayList<>();
    }

    public void with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value));
    }

    public Specification<Booking> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification<Booking>> specs = params.stream()
                .map(BookingSpecification::new)
                .collect(Collectors.toList());

        Specification<Booking> result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {
            result = Specification.where(result)
                    .and(specs.get(i));
        }
        return result;
    }
}
