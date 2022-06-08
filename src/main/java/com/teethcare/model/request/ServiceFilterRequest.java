package com.teethcare.model.request;

import com.teethcare.model.entity.ServiceOfClinic;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServiceFilterRequest {
    private Integer id;
    private String name;
    private Integer clinicID;
    private BigDecimal lowerPrice;
    private BigDecimal upperPrice;

    public List<Predicate<ServiceOfClinic>> predicates() {
        List<Predicate<ServiceOfClinic>> predicates = new ArrayList<>();
        if (id != null) {
            predicates.add(service -> service.getId().toString().contains(id.toString()));
        }
        if (name != null) {
            predicates.add(service -> service.getName().toLowerCase().contains(name.toLowerCase()));
        }
        if (clinicID != null) {
            predicates.add(service -> service.getClinic().getId() == clinicID);
        }
        if (lowerPrice != null && upperPrice != null) {
            predicates.add(service -> service.getPrice().compareTo(lowerPrice) >= 0
                    && service.getPrice().compareTo(upperPrice) <= 0);
        }
        if (lowerPrice != null && upperPrice == null) {
            predicates.add((service) -> service.getPrice().compareTo(lowerPrice) >= 0);
        }
        if (lowerPrice == null && upperPrice != null) {
            predicates.add(service -> service.getPrice().compareTo(upperPrice) <= 0);
        }
        return predicates;
    }
}
