package com.teethcare.specification;


import com.teethcare.exception.BadRequestException;
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
                return builder.equal((root.get("id")), criteria.getValue());
            case "patientName":
                Predicate firstNamePredicate =
                        builder.like((patientJoin. get("firstName")), "%" + criteria.getValue().toString() + "%");
                Predicate lastNamePredicate =
                        builder.like((patientJoin. get("lastName")), "%" + criteria.getValue().toString() + "%");
                return builder.or(firstNamePredicate, lastNamePredicate);
            case "patientPhone":
                return builder.equal(patientJoin. get("phone"), criteria.getValue().toString());
            case "dentistId":
                return builder.equal((dentistJoin.get("id")), criteria.getValue());
            case "customerServiceId":
                return builder.equal((customerServiceJoin.get("id")), criteria.getValue());
            default:
                throw new BadRequestException();
        }
    }
}
