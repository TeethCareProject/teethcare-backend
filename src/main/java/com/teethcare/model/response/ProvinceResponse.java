package com.teethcare.model.response;

import com.teethcare.model.entity.District;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProvinceResponse {
    private int id;
    private String name;
    List<DistrictResponse> districtList;
}
