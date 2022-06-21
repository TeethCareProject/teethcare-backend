package com.teethcare.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class CheckAvailableTimeRequest {
    @NotNull
    Integer clinicId;
    @NotNull
    Long desiredCheckingTime;
}
