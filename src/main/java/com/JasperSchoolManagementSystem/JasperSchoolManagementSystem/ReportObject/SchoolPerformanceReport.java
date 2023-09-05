package com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.ReportObject;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SchoolPerformanceReport {
    private String schoolName;
    private double averageMark;
}
