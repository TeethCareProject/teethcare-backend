package com.teethcare.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "report")
public class Report implements Comparable<Report>{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "feedback_id", referencedColumnName = "id")
    private Feedback feedback;

    @Column(name = "detail")
    private String detail;

    @Column(name = "status")
    private String status;

    @Column(name = "created_time")
    private Timestamp createdTime;

    @Override
    public int compareTo(Report o) {
        return o.createdTime.compareTo(this.createdTime);
    }
}
