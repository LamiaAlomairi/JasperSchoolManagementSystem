package com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Controllers;

import com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Services.ReportService;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/schools")
    public ResponseEntity<String> generateSchoolReport() {
        try {
            String reportPath = reportService.generateReport();
            return ResponseEntity.ok(reportPath);
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error generating report.");
        }
    }

    @GetMapping("/courses")
    public ResponseEntity<String> generateCourseReport() {
        try {
            String reportPath = reportService.courseReport();
            return ResponseEntity.ok(reportPath);
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error generating report.");
        }
    }

    @GetMapping("/averageMarks")
    public ResponseEntity<String> generateAverageMarksReport() {
        try {
            String reportPath = reportService.averageMarksReport();
            return ResponseEntity.ok(reportPath);
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error generating report.");
        }
    }

    @GetMapping("/topPerformingStudents")
    public ResponseEntity<String> generateTopPerformingStudentsReport() {
        try {
            String reportPath = reportService.generateTopPerformingStudentReport();
            return ResponseEntity.ok(reportPath);
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error generating report.");
        }
    }


    @GetMapping("/studentPerformance")
    public ResponseEntity<String> generateStudentPerformanceReport() {
        try {
            String reportPath = reportService.generateStudentPerformanceReport();
            return ResponseEntity.ok(reportPath);
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error generating report.");
        }
    }

    @GetMapping("/schoolStudents")
    public ResponseEntity<String> generateSchoolStudentsReport() {
        try {
            String reportPath = reportService.generateSchoolStudentsReport();
            return ResponseEntity.ok(reportPath);
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error generating report.");
        }
    }


    @GetMapping("/schoolCourses")
    public ResponseEntity<String> generateSchoolCoursePerformanceReport() {
        try {
            String reportPath = reportService.generateSchoolCoursePerformanceReport();
            return ResponseEntity.ok(reportPath);
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error generating report.");
        }
    }

    @GetMapping("/schoolPerformance")
    public ResponseEntity<String> generateSchoolPerformanceReport() {
        try {
            String reportPath = reportService.generateSchoolPerformanceReport();
            return ResponseEntity.ok(reportPath);
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error generating report.");
        }
    }


    @GetMapping("/generateGradeDistributionReport")
    public String generateGradeDistributionReport() {
        try {
            return reportService.generateGradeDistributionReport();
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
            return "Error generating the report.";
        }
    }
}
