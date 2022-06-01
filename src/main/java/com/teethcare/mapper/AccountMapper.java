package com.teethcare.mapper;

import com.teethcare.model.entity.*;
import com.teethcare.model.request.ManagerRegisterRequest;
import com.teethcare.model.request.PatientRegisterRequest;
import com.teethcare.model.response.*;
import jdk.jfr.Name;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.name", target = "roleName")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth", dateFormat = "dd/MM/yyyy")
    AccountResponse mapAccountToAccountResponse(Account account);

    @InheritConfiguration(name = "mapAccountToAccountResponse")
    List<AccountResponse> mapAccountListToAccountResponseList(List<Account> account);

    //    Patient
    @InheritConfiguration(name = "mapAccountToAccountResponse")
    @Named(value = "mapPatientToPatientResponse")
    PatientResponse mapPatientToPatientResponse(Patient patient);

    @Named(value = "mapPatientToPatientResponseForBooking")
    @InheritConfiguration(name = "mapAccountToAccountResponse")
    @Mapping(source = "status", target = "status", ignore = true)
    @Mapping(source = "username", target = "username", ignore = true)
    @Mapping(source = "avatarImage", target = "avatarImage", ignore = true)
    @Mapping(source = "role.id", target = "roleId", ignore = true)
    @Mapping(source = "role.name", target = "roleName", ignore = true)
    PatientResponse mapPatientToPatientResponseForBooking(Patient patient);

    @IterableMapping(qualifiedByName = "mapPatientToPatientResponse")
    List<PatientResponse> mapPatientListToPatientResponseList(List<Patient> patients);

    @InheritConfiguration(name = "mapAccountToAccountResponse")
    @Mapping(source = "manager.id", target = "id")
    @Mapping(source = "manager.status", target = "status")
    @Mapping(source = "manager.phone", target = "phone")
    @Mapping(source = "manager.role.id", target = "roleId")
    @Mapping(source = "manager.role.name", target = "roleName")
    @Mapping(target = "clinic", expression = "java(clinicInfoResponse)")
    ManagerResponse mapManagerToManagerResponse(Manager manager, ClinicInfoResponse clinicInfoResponse);

    @InheritConfiguration(name = "mapAccountToAccountResponse")
    List<ManagerResponse> mapManagerListToManagerResponseList(List<Manager> managers);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "role", ignore = true)
    @Mapping(source = "phoneNumber", target = "phone")
    @InheritConfiguration(name = "mapAccountToAccountResponse")
    Patient mapPatientRegisterRequestToPatient(PatientRegisterRequest patientRegisterRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "role", ignore = true)
    @Mapping(source = "phoneNumber", target = "phone")
    @Mapping(source = "email", target = "email")
    @InheritConfiguration(name = "mapAccountToAccountResponse")
    Manager mapManagerRegisterRequestToManager(ManagerRegisterRequest managerRegisterRequest);

    @InheritConfiguration(name = "mapAccountToAccountResponse")
    @Mapping(source = "clinic.id", target = "clinicId")
    @Mapping(source = "clinic.name", target = "clinicName")
    DentistResponse mapDentistToDentistResponse(Dentist dentist);

    @InheritConfiguration(name = "mapAccountToAccountResponse")
    @Mapping(source = "clinic.id", target = "clinicId")
    @Mapping(source = "clinic.name", target = "clinicName")
    CustomerServiceResponse mapCustomerServiceToCustomerServiceResponse(CustomerService customerService);


}
