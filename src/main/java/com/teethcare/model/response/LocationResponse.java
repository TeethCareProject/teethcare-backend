package com.teethcare.model.response;

import com.teethcare.model.entity.Ward;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationResponse {
    private int id;
    private Float latitude;
    private Float longitude;
    private String address;
    private Ward ward;
}
