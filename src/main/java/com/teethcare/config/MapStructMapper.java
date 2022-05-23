package com.teethcare.config;

import com.teethcare.model.dto.ClinicPreDTO;
import com.teethcare.model.entity.Account;
import com.teethcare.model.entity.Clinic;
import com.teethcare.model.entity.CustomerService;
import com.teethcare.model.entity.Dentist;
import com.teethcare.model.response.AccountResponse;
import com.teethcare.model.response.CustomerServiceResponse;
import com.teethcare.model.response.DentistResponse;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MapStructMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "managerId", ignore = true)
    @Mapping(target = "locationId", ignore = true)
    @Mapping(target = "avgRatingScore", ignore = true)
    @Mapping(target = "taxCode", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateClinicFromDTO(ClinicPreDTO dto, @MappingTarget Clinic entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "dateOfBirth", target = "dateOfBirth", dateFormat = "MM/dd/yyyy")
    AccountResponse mapAccountToAccountDTO(Account account);

//    @Mapping(source = "dateOfBirth", target = "dateOfBirth", dateFormat = "MM/dd/yyyy")
    List<AccountResponse> mapAccountListToAccountDTOList(List<Account> account);

//    default EmployeeFullNameDTO map(Employee employee) {
//        EmployeeFullNameDTO employeeInfoDTO = new EmployeeFullNameDTO();
//        employeeInfoDTO.setFullName(employee.getFirstName() + " " + employee.getLastName());
//
//        return employeeInfoDTO;
//    }

    @InheritConfiguration(name = "mapAccountToAccountDTO")
    @Mapping(source = "clinic.id", target = "clinicId")
    @Mapping(source = "clinic.name", target = "clinicName")
    DentistResponse mapDentistToDentistResponse(Dentist dentist);

    @InheritConfiguration(name = "mapAccountToAccountDTO")
    @Mapping(source = "clinic.id", target = "clinicId")
    @Mapping(source = "clinic.name", target = "clinicName")
    CustomerServiceResponse mapCustomerServiceToCustomerServiceResponse(CustomerService customerService);
}
