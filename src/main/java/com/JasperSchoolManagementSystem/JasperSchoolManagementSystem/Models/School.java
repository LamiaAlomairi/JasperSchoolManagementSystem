package com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Entity
public class School {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
    private List<Student> students;

    @OneToMany(mappedBy = "school", cascade = CascadeType.ALL)
    private List<Course> courses;
}
