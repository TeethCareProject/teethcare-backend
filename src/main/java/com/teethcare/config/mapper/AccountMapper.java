package com.teethcare.config.mapper;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Manager;
import com.teethcare.model.entity.Patient;
import com.teethcare.model.entity.Role;
import com.teethcare.model.request.ManagerRegisterRequest;
import com.teethcare.model.request.PatientRegisterRequest;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.model.response.PatientResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "dateOfBirth", target = "dateOfBirth", dateFormat = "dd/MM/yyyy")
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.name", target = "roleName")
    AccountResponse mapAccountToAccountResponse(Account account);

    List<AccountResponse> mapAccountListToAccountResponseList(List<Account> account);

    //    Patient
    @InheritConfiguration(name = "mapAccountToAccountResponse")
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


    //    @Mapping(source = "dateOfBirth", target = "dateOfBirth", dateFormat = "MM/dd/yyyy")
    List<AccountResponse> mapAccountListToAccountResponseList(List<Account> account);

    @InheritConfiguration(name = "mapAccountToAccountResponse")
    @Mapping(source = "clinic.id", target = "clinicId")
    @Mapping(source = "clinic.name", target = "clinicName")
    DentistResponse mapDentistToDentistResponse(Dentist dentist);

    @InheritConfiguration(name = "mapAccountToAccountResponse")
    @Mapping(source = "clinic.id", target = "clinicId")
    @Mapping(source = "clinic.name", target = "clinicName")
    CustomerServiceResponse mapCustomerServiceToCustomerServiceResponse(CustomerService customerService);
}
