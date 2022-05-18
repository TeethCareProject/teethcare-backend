package com.teethcare.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "account")
public class Account {
    @Id
    private String userId;
    @Column(name = "role_id", length = 72, nullable = false)
    private String password;
    @Column(name = "role_id", nullable = false)
    private String roleId;
    @Column(name = "first_name",length = 40, nullable = false)
    private String fistName;
    @Column(name = "last_name",length = 10, nullable = false)
    private String lastName;
    @Column(name = "gender")
    private boolean gender;
    @Column(name = "avatar_image")
    private String avatarImage;
    @Column(name = "date_of_birth")
    private Date dateOfBirth;
    private boolean status;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
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
//    id  varchar(72) UNIQUE NOT NULL,
//	[password] varchar(72) NOT NULL, --define hash function
//    role_id int NOT NULL,
//    first_name nvarchar(40) NOT NULL, --ten
//    last_name nvarchar(10) NOT NULL, --ho
//    gender bit,
//    avatar_image text DEFAULT NULL,
//    date_of_birth date DEFAULT NULL,
//    status bit DEFAULT 1 -- int hay bit
//


}
