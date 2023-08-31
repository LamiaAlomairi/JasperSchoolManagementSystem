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

    @GetMapping
    public ResponseEntity<String> generateSchoolReport() {
        try {
            String reportPath = reportService.generateReport();
            return ResponseEntity.ok(reportPath);
        } catch (FileNotFoundException | JRException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error generating report.");
        }
    }
}