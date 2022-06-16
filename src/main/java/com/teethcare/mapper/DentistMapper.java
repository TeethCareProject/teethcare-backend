package com.teethcare.mapper;

import com.teethcare.model.entity.Dentist;
import com.teethcare.model.response.DentistResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper
public interface DentistMapper {
//    "dateOfBirth": "2002-10-26T00:00:00.000+07:00",
//            "email": null,
//            "phone": null,
//            "gender": "FEMALE",
//            "status": "ACTIVE",
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
