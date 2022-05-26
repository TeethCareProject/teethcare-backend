package com.teethcare.model.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class BookingRequest {
    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;


}
