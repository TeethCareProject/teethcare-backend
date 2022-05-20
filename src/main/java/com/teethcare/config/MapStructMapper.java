package com.teethcare.config;

import com.teethcare.model.dto.ClinicDTO;
import com.teethcare.model.entity.Clinic;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "managerId", ignore = true)
    @Mapping(target = "locationId", ignore = true)
    @Mapping(target = "avgRatingScore", ignore = true)
    @Mapping(target = "taxCode", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateClinicFromDTO(ClinicDTO dto, @MappingTarget Clinic entity);
}
