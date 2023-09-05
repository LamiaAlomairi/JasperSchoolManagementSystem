package com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.ReportObject;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CourseGradeDistribution {
    private String courseName;
    private String grade;
    private int count;
}
