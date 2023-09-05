package com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.ReportObject;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SchoolStudentsReport {
    private String schoolName;
    private int totalStudents;
}
