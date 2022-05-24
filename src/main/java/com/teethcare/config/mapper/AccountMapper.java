package com.teethcare.config.mapper;

import com.teethcare.config.model.entity.Account;
import com.teethcare.config.model.entity.Manager;
import com.teethcare.config.model.entity.Patient;
import com.teethcare.config.model.entity.Role;
import com.teethcare.config.model.request.ManagerRegisterRequest;
import com.teethcare.config.model.request.PatientRegisterRequest;
import com.teethcare.config.model.response.AccountResponse;
import com.teethcare.config.model.response.PatientResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", imports = Role.class)
public interface AccountMapper {
    //    Account
    @Mapping(source = "role", target = "role", qualifiedByName = "mapRoleToString")
    AccountResponse mapAccountToAccountResponse(Account account);

    @Mapping(source = "role", target = "role", qualifiedByName = "mapRoleToString")
    List<AccountResponse> mapAccountListToAccountResponseList(List<Account> accounts);

    //    Patient
    @InheritConfiguration(name = "mapAccountToAccountResponse")
    @Mapping(source = "role", target = "role", qualifiedByName = "mapRoleToString")
    PatientResponse mapPatientToPatientResponse(Patient patient);

    @InheritConfiguration(name = "mapAccountListToAccountResponseList")
    List<PatientResponse> mapPatientListToPatientResponseList(List<Patient> patients);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "role", ignore = true)
    @Mapping(source = "phoneNumber", target = "phone")
    Patient mapPatientRegisterRequestToPatient(PatientRegisterRequest patientRegisterRequest);


    //    Manager
//    @InheritConfiguration(name = "mapAccountToAccountResponse")
//    @Mapping(source = "role", target = "role", qualifiedByName = "mapRoleToString")
//    ManagerResponse mapManagerToManagerResponse(Manager manager);

//    @InheritConfiguration(name = "mapAccountListToAccountResponseList")
//    @Mapping(source = "clinic", target = "clinic", qualifiedByName = "mapClinicToClinicInfoResponse")
//    List<ManagerResponse> mapManagerListToManagerResponseList(List<Manager> managers);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "role", ignore = true)
    @Mapping(source = "phoneNumber", target = "phone")
    Manager mapManagerRegisterRequestToManager(ManagerRegisterRequest managerRegisterRequest);

    //    Methods
    @Named("mapRoleToString")
    static String mapRoleToString(Role role) {
        return role.getName();
    }

    @Named("mapStringToRole")
    static Role mapStringToRole(String role) {
        return new Role(3, role);
    }


}