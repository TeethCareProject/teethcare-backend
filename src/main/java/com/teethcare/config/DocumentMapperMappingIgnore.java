package com.teethcare.config;

import com.teethcare.model.dto.ClinicDTO;
import com.teethcare.model.entity.Clinic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DocumentMapperMappingIgnore {
    DocumentMapperMappingIgnore INSTANCE =
            Mappers.getMapper(DocumentMapperMappingIgnore.class);
    @Mapping(target = "managerId", ignore = true)
    @Mapping(target = "locationId", ignore = true)
    @Mapping(target = "avgRatingScore", ignore = true)
    @Mapping(target = "taxCode", ignore = true)
    @Mapping(target = "status", ignore = true)
    Clinic mapClinicDTOToClinic(ClinicDTO clinicDTO);
}
