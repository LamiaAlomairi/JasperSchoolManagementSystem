package com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.ReportObject;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CoursesReport {
    String courseName;
    Double obtainedMark;
    String grade;
}
