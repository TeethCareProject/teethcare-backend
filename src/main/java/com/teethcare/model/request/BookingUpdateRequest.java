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
    private List<Integer> serviceIds;
    private Integer dentistId;
    private Long examinationTime;
    private String note;
    private Integer version;
}
