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
//    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "dateOfBirth", target = "dateOfBirth", dateFormat = "MM/dd/yyyy")
    AccountResponse mapAccountToAccountDTO(Account account);

    //    @Mapping(source = "dateOfBirth", target = "dateOfBirth", dateFormat = "MM/dd/yyyy")
    List<AccountResponse> mapAccountListToAccountDTOList(List<Account> account);

    @InheritConfiguration(name = "mapAccountToAccountDTO")
    @Mapping(source = "clinic.id", target = "clinicId")
    @Mapping(source = "clinic.name", target = "clinicName")
    DentistResponse mapDentistToDentistResponse(Dentist dentist);

    @InheritConfiguration(name = "mapAccountToAccountDTO")
    @Mapping(source = "clinic.id", target = "clinicId")
    @Mapping(source = "clinic.name", target = "clinicName")
    CustomerServiceResponse mapCustomerServiceToCustomerServiceResponse(CustomerService customerService);
}
