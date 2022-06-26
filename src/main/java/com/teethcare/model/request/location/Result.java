package com.teethcare.model.request.location;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Result {
    @JsonProperty("formatted_address")
    private String formattedAddress;
    private Geometry geometry;
}
