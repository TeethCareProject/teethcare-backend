package com.teethcare.model.dto.account;

import java.util.Date;

public class DentistDTO extends AccountDTO {
    private String specialization;

    private String description;

    private String phoneNumber;

    public DentistDTO(String id, String password, int roleId, String fistName, String lastName,
                      boolean gender, String avatarImage, Date dateOfBirth, int status,
                      String specialization, String description, String phoneNumber) {
        super(id, password, roleId, fistName, lastName, gender, avatarImage, dateOfBirth, status);
        this.specialization = specialization;
        this.description = description;
        this.phoneNumber = phoneNumber;
    }

    public DentistDTO() {
        super();
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
