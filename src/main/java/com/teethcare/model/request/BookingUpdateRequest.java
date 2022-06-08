package com.teethcare.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookingUpdateRequest {
    @NotNull
    private Integer bookingId;
    @NotNull
    private List<Integer> serviceIds;

    private Integer dentistId;
    private Long examinationTime;
}
