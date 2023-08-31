package com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Repositories;

import com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {
}
