package com.teethcare.model.request;

import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.ServiceOfClinic;
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
        if (name != null) {
            predicate = predicate.and((clinic) -> clinic.getName().toUpperCase().contains(name
                    .replaceAll("\\s\\s+", " ").trim().toUpperCase()));
        }
        if (status != null) {
            predicate = predicate.and((clinic) -> clinic.getStatus().equalsIgnoreCase(status.trim()));
        }
        if (provinceId != null) {
            predicate = predicate.and((clinic) -> clinic.getLocation()
                    .getWard().getDistrict().getProvince().getId() == provinceId);
        }
        if (districtId != null) {
            predicate = predicate.and((clinic) -> clinic.getLocation().getWard().getDistrict().getId() == districtId);
        }
        if (wardId != null) {
            predicate = predicate.and((clinic) -> clinic.getLocation().getWard().getId() == wardId);
        }
        if (id != null) {
            predicate = predicate.and((clinic) -> clinic.getId().toString().toUpperCase()
                    .contains(id.trim().toUpperCase()));
        }
        if (serviceId != null) {
            predicate = predicate.and(clinic -> {
                List<ServiceOfClinic> serviceOfClinicList = clinic.getServiceOfClinic();
                return serviceOfClinicList.stream().anyMatch(service -> service.getId().toString().toUpperCase()
                        .contains(serviceId.trim().toUpperCase()));
            });
        }
        if (serviceName != null) {
            predicate = predicate.and(clinic -> {
                List<ServiceOfClinic> serviceOfClinicList = clinic.getServiceOfClinic();
                return serviceOfClinicList.stream().anyMatch(service -> service.getName().toUpperCase().contains(serviceName
                        .replaceAll("\\s\\s+", " ").trim().toUpperCase()));
            });
        }
        return predicate;
    }
}
