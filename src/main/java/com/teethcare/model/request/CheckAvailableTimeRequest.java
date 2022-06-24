package com.teethcare.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CheckAvailableTimeRequest {
    @NotNull
    Integer clinicId;
    @NotNull
    Long desiredCheckingTime;
}
