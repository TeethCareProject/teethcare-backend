package com.teethcare.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ClinicRequest {

    @JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("imageUrl")
    private String imageUrl;

    @Length(max = 150)
    private String clinicAddress;

    private Integer wardId;

    private String email;

    private String phone;

    private Integer bookingGap;

    private Integer expiredDay;

    private String facebookPageId;

    private Long startTimeShift1;

    private Long endTimeShift1;

    private Long startTimeShift2;

    private Long endTimeShift2;

    @Override
    public String toString() {
        return "ClinicRequest{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", clinicAddress='" + clinicAddress + '\'' +
                ", wardId=" + wardId +
                '}';
    }
}
