package com.teethcare.model.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckAvailableTimeRequest {
    Integer clinicId;
    Long desiredCheckingTime;
}
