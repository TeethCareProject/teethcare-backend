package com.teethcare.scheduling;

import com.teethcare.model.entity.Voucher;
import com.teethcare.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@RequiredArgsConstructor
@EnableAsync
public class CheckVoucherExpiredScheduling {
    private final VoucherService voucherService;

    @Async
    @Transactional
    @Scheduled(fixedDelay = 1_000, initialDelay = 1_000)
    public void checkExpiredVoucher() {
        long now = System.currentTimeMillis() / 1_000;
        List<Voucher> vouchers = voucherService.findAllVouchersByExpiredTime(now * 1_000);
        if (vouchers.size() > 0) {
            System.out.println(vouchers.get(0).getExpiredTime().getTime());
        }
        vouchers.forEach(voucherService::deactivate);
    }
}
