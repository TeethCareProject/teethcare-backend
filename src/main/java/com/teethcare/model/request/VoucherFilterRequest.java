package com.teethcare.model.request;

import com.teethcare.model.entity.Voucher;
import com.teethcare.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Predicate;

@Getter
@Setter
public class VoucherFilterRequest {
    private String voucherCode;
    public Predicate<Voucher> getPredicate() {
        Predicate<Voucher> predicate = voucher -> true;
        if (voucherCode != null) {
            predicate = predicate.and(voucher -> StringUtils.containsIgnoreCase(voucher.getVoucherCode(), voucherCode));
        }
        return predicate;
    }
}
