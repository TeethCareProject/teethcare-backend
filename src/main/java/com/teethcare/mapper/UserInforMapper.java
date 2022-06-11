package com.teethcare.mapper;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Dentist;
import com.teethcare.model.response.UserInforResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserInforMapper {

    @Named(value = "mapAccountToUserInforResponse")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    UserInforResponse mapAccountToUserInforResponse(Account account);

    @Named(value = "mapDentistToUserInforResponse")
    @Mapping(source = "id", target = "id")
    @Mapping(source = "firstName", target = "firstName")
    @Mapping(source = "lastName", target = "lastName")
    @Mapping(source = "specialization", target = "specialization")
    UserInforResponse mapDentistToUserInforResponse(Dentist dentist);
}
