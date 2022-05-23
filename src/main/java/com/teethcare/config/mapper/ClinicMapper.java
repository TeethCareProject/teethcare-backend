package com.teethcare.config.mapper;

import com.teethcare.model.entity.Clinic;
import com.teethcare.model.request.ClinicRequest;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ClinicMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "managerId", ignore = true)
    @Mapping(target = "locationId", ignore = true)
    @Mapping(target = "avgRatingScore", ignore = true)
    @Mapping(target = "taxCode", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateClinicFromDTO(ClinicRequest dto, @MappingTarget Clinic entity);
}
