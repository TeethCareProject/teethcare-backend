package com.teethcare.model.dto;


import java.util.Date;

public class AccountDTO {
    private String id;
    private String password;
    private int roleId;
    private String fistName;
    private String lastName;
    private boolean gender;
    private String avatarImage;
    private Date dateOfBirth;
    private boolean status;

    public AccountDTO(String id, String password, int roleId, String fistName, String lastName, boolean gender, String avatarImage, Date dateOfBirth, boolean status) {
        this.id = id;
        this.password = password;
        this.roleId = roleId;
        this.fistName = fistName;
        this.lastName = lastName;
        this.gender = gender;
        this.avatarImage = avatarImage;
        this.dateOfBirth = dateOfBirth;
        this.status = status;
    }

    public AccountDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getFistName() {
        return fistName;
    }

    public void setFistName(String fistName) {
        this.fistName = fistName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean getGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
