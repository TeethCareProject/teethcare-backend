package com.teethcare.mapper;

import com.teethcare.model.entity.Voucher;
import com.teethcare.model.request.VoucherRequest;
import com.teethcare.model.request.VoucherUpdateRequest;
import com.teethcare.model.response.VoucherResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        config = ConfigurationMapper.class)
public interface VoucherMapper {
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Voucher mapVoucherRequestToVoucher(VoucherRequest voucherRequest);

    @Named("mapVoucherToVoucherResponse")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    VoucherResponse mapVoucherToVoucherResponse(Voucher voucher);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateVoucherFromVoucherUpdateRequest(VoucherUpdateRequest voucherUpdateRequest, @MappingTarget Voucher voucher);
}
