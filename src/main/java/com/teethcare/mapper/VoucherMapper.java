package com.teethcare.mapper;

import com.teethcare.model.entity.Voucher;
import com.teethcare.model.request.VoucherRequest;
import com.teethcare.model.response.VoucherResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        config = ConfigurationMapper.class, uses = {ClinicMapper.class})
public interface VoucherMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "expiredTime", ignore = true)
    Voucher mapVoucherRequestToVoucher(VoucherRequest voucherRequest);

    @Named("mapVoucherToVoucherResponse")
    @Mapping(target = "clinic", source = "clinic", qualifiedByName = "mapClinicToClinicInfoResponse")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    VoucherResponse mapVoucherToVoucherResponse(Voucher voucher);
}
