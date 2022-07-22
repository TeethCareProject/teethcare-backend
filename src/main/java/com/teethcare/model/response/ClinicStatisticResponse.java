package com.teethcare.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClinicStatisticResponse {
    private BookingStatisticResponse bookingStatisticResponse;
    private int bookingTotal;
    private int dentistTotal;
    private int customerSeviceTotal;
}
