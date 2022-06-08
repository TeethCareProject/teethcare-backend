package com.teethcare.model.request;

import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.ServiceOfClinic;
import com.teethcare.utils.StringUtils;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.function.Predicate;

@Getter
@Setter
@NoArgsConstructor
public class ClinicFilterRequest {
    private String id;
    private String name;
    private Integer wardId;
    private Integer districtId;
    private Integer provinceId;
    private String serviceId;
    private String serviceName;
    String status;

    public Predicate<Clinic> getPredicate() {
        Predicate<Clinic> predicate = clinic -> true;
        if (id != null) {
            predicate = predicate.and(clinic -> StringUtils.containsIgnoreCase(clinic.getId().toString(), id));
        }
        if (name != null) {
            predicate = predicate.and(clinic -> StringUtils.containsIgnoreCase(clinic.getName(), name));
        }
        if (status != null) {
            predicate = predicate.and(clinic -> StringUtils.equalsIgnoreCase(clinic.getStatus(), status));
        }
        if (provinceId != null) {
            predicate = predicate.and(clinic -> clinic.getLocation()
                    .getWard().getDistrict().getProvince().getId() == provinceId);
        }
        if (districtId != null) {
            predicate = predicate.and(clinic -> clinic.getLocation().getWard().getDistrict().getId() == districtId);
        }
        if (wardId != null) {
            predicate = predicate.and(clinic -> clinic.getLocation().getWard().getId() == wardId);
        }
        if (serviceId != null) {
            predicate = predicate.and(clinic -> {
                List<ServiceOfClinic> serviceOfClinicList = clinic.getServiceOfClinic();
                return serviceOfClinicList.stream()
                        .anyMatch(service -> StringUtils.containsIgnoreCase(service.getId().toString(), serviceId));
            });
        }
        if (serviceName != null) {
            predicate = predicate.and(clinic -> {
                List<ServiceOfClinic> serviceOfClinicList = clinic.getServiceOfClinic();
                return serviceOfClinicList.stream()
                        .anyMatch(service -> StringUtils.containsIgnoreCase(service.getName(), serviceName));
            });
        }
        return predicate;
    }
}
