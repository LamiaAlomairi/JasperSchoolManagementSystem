package com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.ReportObject;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CourseAverageReport {
    String courseName;
    Double averageMark;
}
