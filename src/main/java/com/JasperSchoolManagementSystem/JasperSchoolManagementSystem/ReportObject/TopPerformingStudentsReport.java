package com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.ReportObject;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TopPerformingStudentsReport {
    String schoolName;
    String studentName;
    Long rollNumber;
}
