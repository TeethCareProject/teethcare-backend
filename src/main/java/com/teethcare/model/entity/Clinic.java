package com.teethcare.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor
@Entity
@Table(name = "clinic")
public class Clinic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "manager_id", length = 72, nullable = false)
    @NotEmpty
    private String managerId;

    @Column(name = "location_id", nullable = false)
    @NotEmpty
    private String locationId;

    @NotEmpty
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @NotEmpty
    @Column(name = "description")
    private String description;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "tax_code", length = 13, unique = true, nullable = false)
    private String taxCode;

    @Column(name = "avg_rating_score")
    @Max(value = 5)
    @Min(value = 0)
    private float avgRatingScore;

    @Column(name = "status")
    private boolean status;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "clinic",
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH})
    private List<Dentist> dentists;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "clinic",
            cascade = {CascadeType.DETACH, CascadeType.MERGE,
                    CascadeType.PERSIST, CascadeType.REFRESH})
    private List<CustomerService> customerServices;

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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Clinic{" +
                "id=" + id +
                ", managerId='" + managerId + '\'' +
                ", locationId='" + locationId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", taxCode='" + taxCode + '\'' +
                ", avgRatingScore=" + avgRatingScore +
                ", status=" + status +
                ", dentists=" + dentists +
                ", customerServices=" + customerServices +
                '}';
    }
}
