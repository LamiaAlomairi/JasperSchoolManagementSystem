package com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Repositories;

import com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Models.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
}
