package com.teethcare.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingStatisticResponse {
    private float pendingBooking;
    private float processingBooking;
    private float doneBooking;
    private float failedBooking;
}
