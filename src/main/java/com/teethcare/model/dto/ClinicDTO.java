package com.teethcare.model.dto;

public class ClinicDTO {

    private int id;

    private String managerId;

    private String locationId;

    private String name;

    private String description;

    private boolean gender;

    private String imageUrl;

    private String taxCode;

    private float avgRatingScore;

    private boolean status;

    public ClinicDTO(int id, String managerId, String locationId, String name, String description, boolean gender, String imageUrl, String taxCode, float avgRatingScore, boolean status) {
        this.id = id;
        this.managerId = managerId;
        this.locationId = locationId;
        this.name = name;
        this.description = description;
        this.gender = gender;
        this.imageUrl = imageUrl;
        this.taxCode = taxCode;
        this.avgRatingScore = avgRatingScore;
        this.status = status;
    }

    public ClinicDTO() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getManagerId() {
        return managerId;
    }

    public void setManagerId(String managerId) {
        this.managerId = managerId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public float getAvgRatingScore() {
        return avgRatingScore;
    }

    public void setAvgRatingScore(float avgRatingScore) {
        this.avgRatingScore = avgRatingScore;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
