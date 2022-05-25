package com.teethcare.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ClinicRequest {

    @JsonProperty("id")
    private int id;

    @JsonProperty("name")
    @NotBlank
    private String name;

    @JsonProperty("description")
    @NotBlank
    private String description;

    @JsonProperty("imageUrl")
    @NotBlank
    private String imageUrl;

    @Override
    public String toString() {
        return "ClinicRequest{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
