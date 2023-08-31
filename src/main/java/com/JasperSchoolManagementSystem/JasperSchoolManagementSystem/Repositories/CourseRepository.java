package com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Repositories;

import com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Models.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
