package com.teethcare.model.request.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class LocationRequest {
    @JsonProperty("results")
    List<Result> results;
}
