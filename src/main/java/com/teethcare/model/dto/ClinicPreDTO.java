package com.teethcare.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class ClinicDTO {

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
        return "ClinicDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
