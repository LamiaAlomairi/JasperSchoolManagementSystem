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
}
