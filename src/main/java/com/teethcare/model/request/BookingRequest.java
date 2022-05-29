package com.teethcare.model.request;

import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequest {

    @NotNull
    private int serviceId;

    private String description;

    @NotNull
    private long desiredCheckingTime;

}
