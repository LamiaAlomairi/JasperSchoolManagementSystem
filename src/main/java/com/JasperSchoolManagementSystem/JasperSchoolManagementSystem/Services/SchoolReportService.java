package com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Services;

import com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Models.School;
import com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Models.Student;
import com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.ReportObject.SchoolsReport;
import com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Repositories.SchoolRepository;
import com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Repositories.StudentRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SchoolReportService {
    @Autowired
    SchoolRepository schoolRepository;
    @Autowired
    StudentRepository studentRepository;

    String pathToReports = "C:\\Users\\dell\\Desktop\\Project\\JasperSchoolManagementSystem\\JasperSchoolManagementSystem\\Report";

    public String generateReport() throws FileNotFoundException, JRException {
        List<School> schoolList = schoolRepository.findAll();
        List<Student> studentList = studentRepository.findAll();

        List<SchoolsReport> schoolsReportList = new ArrayList<>(); // New list to hold reports

        for (School s : schoolList) {
            for (Student student : studentList) {
                if (student.getSchool().getId().equals(s.getId())) {
                    SchoolsReport schoolsReport = SchoolsReport.builder()
                            .schoolName(s.getName())
                            .studentName(student.getName())
                            .rollNumber(student.getRollNumber())
                            .build();
                    schoolsReportList.add(schoolsReport); // Add report to the list
                }
            }
        }

        File file = ResourceUtils.getFile("classpath:SchoolsReport.jrxml");


        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Create a data source from the reports list
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(schoolsReportList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CreatedBy", "Lamia");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, pathToReports +"\\SchoolReport.pdf");
        return "Report generated: " + pathToReports + "\\SchoolReport.pdf";
    }
}
