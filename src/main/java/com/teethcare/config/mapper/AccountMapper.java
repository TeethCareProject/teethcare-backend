package com.teethcare.config.mapper;

import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.Dentist;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.model.response.CustomerServiceResponse;
import com.teethcare.model.response.DentistResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "dateOfBirth", target = "dateOfBirth", dateFormat = "dd/MM/yyyy")
    @Mapping(source = "role.id", target = "roleId")
    @Mapping(source = "role.name", target = "roleName")
    AccountResponse mapAccountToAccountResponse(Account account);


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
