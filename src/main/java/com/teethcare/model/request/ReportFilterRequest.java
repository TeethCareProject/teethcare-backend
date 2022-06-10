package com.teethcare.model.request;

import com.teethcare.model.entity.Report;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ReportFilterRequest {
    private Integer id;
    private String status;
    private Integer clinicID;
    private String clinicName;

    public List<Predicate<Report>> requestPredicate() {
        List<Predicate<Report>> predicates = new ArrayList<>();
        if (id != null) {
            predicates.add(report -> Integer.toString(report.getId()).contains(Integer.toString(id)));
        }
        if (status != null) {
            predicates.add(report -> report.getStatus().equalsIgnoreCase(status));
        }
        if (clinicID != null) {
            predicates.add(report -> report.getFeedback().getBooking().getClinic().getName().toLowerCase().contains(clinicName.toLowerCase()));
        }
        if (clinicName != null) {
            predicates.add(report -> report.getFeedback().getBooking().getClinic().getName().toLowerCase().contains(clinicName.toLowerCase()));
        }
        return predicates;
    }
}
