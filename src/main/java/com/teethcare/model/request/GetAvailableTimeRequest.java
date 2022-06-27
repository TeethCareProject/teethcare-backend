package com.teethcare.model.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetAvailableTimeRequest {
    @NotNull
    private long date;
    @NotNull
    private int clinicId;
}
