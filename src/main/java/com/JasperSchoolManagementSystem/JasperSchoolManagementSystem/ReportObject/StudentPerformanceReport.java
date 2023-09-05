package com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.ReportObject;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class StudentPerformanceReport {
    String studentName;
    Long rollNumber;
    Double averageMark;
}
