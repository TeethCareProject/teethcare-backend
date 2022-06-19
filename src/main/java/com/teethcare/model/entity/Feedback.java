package com.teethcare.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "booking_id", referencedColumnName = "id")
    private Booking booking;

    @Column(name = "detail")
    private String detail;

    @Column(name = "rating_score")
    private int ratingScore;

    @Column(name = "status")
    private String status;

    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "feedback")
    @JsonBackReference
    private List<Report> reports;

    @Column(name = "created_time")
    private Timestamp createdTime;

    public int getId() {
        return id;
    }
}
