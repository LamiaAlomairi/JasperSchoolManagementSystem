package com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long rollNumber;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL)
    private List<Mark> marks;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private School school;
}
