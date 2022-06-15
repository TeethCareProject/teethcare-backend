package com.teethcare.mapper;

import com.teethcare.model.entity.*;
import com.teethcare.model.request.ManagerRegisterRequest;
import com.teethcare.model.request.PatientRegisterRequest;
import com.teethcare.model.request.ProfileUpdateRequest;
import com.teethcare.model.request.StaffRegisterRequest;
import com.teethcare.model.response.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring",
        config = ConfigurationMapper.class)
public interface AccountMapper {
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.name", target = "roleName")
    @Mapping(source = "dateOfBirth", target = "dateOfBirth")
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
    Manager mapManagerRegisterRequestToManager(ManagerRegisterRequest managerRegisterRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "role", ignore = true)
    @Mapping(source = "phoneNumber", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    Dentist mapDentistRegisterRequestToDentist(StaffRegisterRequest dentistRegisterRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "role", ignore = true)
    @Mapping(source = "phoneNumber", target = "phone")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    CustomerService mapCSRegisterRequestToCustomerService(StaffRegisterRequest csRegisterRequest);

    @InheritConfiguration(name = "mapAccountToAccountResponse")
    DentistResponse mapDentistToDentistResponse(Dentist dentist);

    @InheritConfiguration(name = "mapDentistToDentistResponse")
    List<DentistResponse> mapDentistListToDentistResponseList(List<Dentist> dentistList);

    @InheritConfiguration(name = "mapAccountToAccountResponse")
    CustomerServiceResponse mapCustomerServiceToCustomerServiceResponse(CustomerService customerService);

    @InheritConfiguration(name = "mapCustomerServiceToCustomerServiceResponse")
    List<CustomerServiceResponse> mapCustomerServiceListToCustomerServiceResponseList(List<CustomerService> dentistList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "phoneNumber", target = "phone")
    @Mapping(target = "dateOfBirth", ignore = true)
    void updateAccountFromProfileUpdateRequest(ProfileUpdateRequest profileUpdateRequest, @MappingTarget Account account);

}
