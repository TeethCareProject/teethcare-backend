package com.teethcare.model.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.teethcare.model.entity.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Time;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClinicDTO {
    private Integer id;
    private ManagerDTO manager;
    private LocationDTO locationDTO;
    private String name;
    private String description;
    private String imageUrl;
    private String taxCode;
    private Float avgRatingScore;
    private String status;
    private Time startTimeShift1;
    private Time endTimeShift1;
    private Time startTimeShift2;
    private Time endTimeShift2;
    private String email;
    private Integer bookingGap;
    private Integer expiredDay;
}
