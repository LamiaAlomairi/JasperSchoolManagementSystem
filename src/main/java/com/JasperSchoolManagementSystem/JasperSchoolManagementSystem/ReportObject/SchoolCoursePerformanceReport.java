package com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.ReportObject;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SchoolCoursePerformanceReport {
    private String schoolName;
    private String courseName;
    private double averageMark;
}
