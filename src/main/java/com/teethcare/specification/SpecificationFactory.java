package com.teethcare.specification;

import com.teethcare.utils.PatternMatcher;
import org.springframework.data.jpa.domain.Specification;

import java.util.regex.Matcher;

public class SpecificationFactory<T> {
    public Specification<T> getSpecification(String searchParams) {
        CustomizedBuilder<T> builder = new CustomizedBuilder<>();
        Matcher matcher = PatternMatcher.get(searchParams);
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        return builder.build();
    }
}
