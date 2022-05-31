package com.teethcare.mapper;

import com.teethcare.model.entity.*;
import com.teethcare.model.request.CSRegisterRequest;
import com.teethcare.model.request.DentistRegisterRequest;
import com.teethcare.model.request.ManagerRegisterRequest;
import com.teethcare.model.request.PatientRegisterRequest;
import com.teethcare.model.response.*;
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
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.name", target = "roleName")
    PatientResponse mapPatientToPatientResponse(Patient patient);

    @InheritConfiguration(name = "mapAccountToAccountResponse")
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
    Dentist mapDentistRegisterRequestToDentist(DentistRegisterRequest dentistRegisterRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "role", ignore = true)
    @Mapping(source = "phoneNumber", target = "phone")
    @Mapping(source = "email", target = "email")
    CustomerService mapCSRegisterRequestToCustomerService(CSRegisterRequest csRegisterRequest);

    @InheritConfiguration(name = "mapAccountToAccountResponse")
    DentistResponse mapDentistToDentistResponse(Dentist dentist);

    @InheritConfiguration(name = "mapDentistToDentistResponse")
    List<DentistResponse> mapDentistListToDentistResponseList(List<Dentist> dentistList);
    @InheritConfiguration(name = "mapAccountToAccountResponse")
    CustomerServiceResponse mapCustomerServiceToCustomerServiceResponse(CustomerService customerService);

    @InheritConfiguration(name = "mapCustomerServiceToCustomerServiceResponse")
    List<CustomerServiceResponse> mapCustomerServiceListToCustomerServiceResponseList(List<CustomerService> dentistList);
}
