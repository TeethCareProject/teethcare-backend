package com.teethcare.mapper;

import com.teethcare.model.entity.Dentist;
import com.teethcare.model.response.DentistResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring",
        config = ConfigurationMapper.class)
public interface DentistMapper {
    @Named("mapDentistToDentistResponse")
    @Mapping(source = "clinic", target = "clinic", ignore = true)
    @Mapping(source = "avatarImage", target = "avatarImage", ignore = true)
    @Mapping(source = "email", target = "email", ignore = true)
    @Mapping(source = "gender", target = "gender", ignore = true)
    @Mapping(source = "status", target = "status", ignore = true)
    @Mapping(source = "phone", target = "phone", ignore = true)
    @Mapping(source = "dateOfBirth", target = "dateOfBirth", ignore = true)
    DentistResponse mapDentistToDentistResponse(Dentist dentist);
}
