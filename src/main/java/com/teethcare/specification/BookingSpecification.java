package com.teethcare.specification;


import com.teethcare.model.entity.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

public class BookingSpecification implements Specification<Booking> {
    private SearchCriteria criteria;

    public BookingSpecification(SearchCriteria searchCriteria) {
        this.criteria = searchCriteria;
    }

    @Override
    public Predicate toPredicate
            (Root<Booking> root, CriteriaQuery<?> query, CriteriaBuilder builder) {
        Join<Booking, Patient> patientJoin = root.join("patient");
        Join<Booking, Dentist> dentistJoin = root.join("dentist");
        Join<Booking, CustomerService> customerServiceJoin = root.join("customerService");
        switch(criteria.getKey()){
            case "bookingId":
                System.out.println(criteria.getValue());
                System.out.println("Hello");
                return builder.equal((root.get("id")), criteria.getValue());
            case "patientName":
                return builder.like((patientJoin. get("firstName")), "%" + criteria.getValue().toString() + "%");
            case "patientPhone":
                return builder.equal(patientJoin. get("phone"), criteria.getValue().toString());
            case "dentistId":
                return builder.equal((dentistJoin.get("id")), criteria.getValue());
            case "customerServiceId":
                return builder.equal((customerServiceJoin.get("id")), criteria.getValue());


//        if (criteria.getOperation().equalsIgnoreCase(">")) {
//            return builder.greaterThanOrEqualTo(
//                    root.get(criteria.getKey()), criteria.getValue().toString());
//        }
//        else if (criteria.getOperation().equalsIgnoreCase("<")) {
//            return builder.lessThanOrEqualTo(
//                    root.get(criteria.getKey()), criteria.getValue().toString());
//        }
//        else if (criteria.getOperation().equalsIgnoreCase(":")) {
//            if (root.get(criteria.getKey()).getJavaType() == String.class) {
//                return builder.like(
//                        root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
//            } else {
//                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
//            }
        }
        return null;
    }
}
