package com.teethcare.model.request.location;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LocationAPI {
    @JsonProperty("lng")
    private double longitude;
    @JsonProperty("lat")
    private double latitude;
}
