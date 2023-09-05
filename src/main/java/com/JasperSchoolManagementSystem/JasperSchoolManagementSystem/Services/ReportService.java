package com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Services;

import com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Models.Course;
import com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Models.Mark;
import com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Models.School;
import com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Models.Student;
import com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.ReportObject.*;
import com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Repositories.CourseRepository;
import com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Repositories.MarkRepository;
import com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Repositories.SchoolRepository;
import com.JasperSchoolManagementSystem.JasperSchoolManagementSystem.Repositories.StudentRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

@Service
public class ReportService {
    @Autowired
    SchoolRepository schoolRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    CourseRepository courseRepository;
    @Autowired
    MarkRepository markRepository;

    String pathToReports = "C:\\Users\\dell\\Desktop\\Project\\JasperSchoolManagementSystem\\JasperSchoolManagementSystem\\Report";

    /* Schools Report ------------------------------------------------------------- */
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

    /* Courses Report ------------------------------------------------------------- */
    public String courseReport() throws FileNotFoundException, JRException {
        List<Course> courseList = courseRepository.findAll();
        List<Mark> markList = markRepository.findAll();

        List<CoursesReport> coursesReportList = new ArrayList<>(); // New list to hold reports

        for (Course c : courseList) {
            for (Mark mark : markList) {
                if (mark.getCourse().getId().equals(c.getId())) {
                    CoursesReport coursesReport = CoursesReport.builder()
                            .courseName(c.getName())
                            .obtainedMark(mark.getCourseMark())
                            .grade(mark.getGrade())
                            .build();
                    coursesReportList.add(coursesReport); // Add report to the list
                }
            }
        }

        File file = ResourceUtils.getFile("classpath:CoursesReport.jrxml");


        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Create a data source from the reports list
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(coursesReportList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CreatedBy", "Lamia");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, pathToReports +"\\CourseReport.pdf");
        return "Report generated: " + pathToReports + "\\CourseReport.pdf";
    }

    /* Average Marks Report ------------------------------------------------------------- */
    public String averageMarksReport() throws FileNotFoundException, JRException {
        List<Mark> markList = markRepository.findAll();

        Map<String, List<Double>> courseMarksMap = new HashMap<>();

    // Group marks by course name
        for (Mark mark : markList) {
            String courseName = mark.getCourse().getName();
            List<Double> courseMarks = courseMarksMap.getOrDefault(courseName, new ArrayList<>());
            courseMarks.add(mark.getCourseMark());
            courseMarksMap.put(courseName, courseMarks);
        }

        List<CourseAverageReport> courseAverageReportList = new ArrayList<>();

        // Calculate the average marks for each course
        for (Map.Entry<String, List<Double>> entry : courseMarksMap.entrySet()) {
            String courseName = entry.getKey();
            List<Double> marksForCourse = entry.getValue();

            if (!marksForCourse.isEmpty()) {
                Double sumMarks = 0.0;

                for (Double mark : marksForCourse) {
                    sumMarks += mark;
                }

                Double averageMark = sumMarks / marksForCourse.size();

                CourseAverageReport courseAverageReport = CourseAverageReport.builder()
                        .courseName(courseName)
                        .averageMark(averageMark)
                        .build();
                courseAverageReportList.add(courseAverageReport);
            }
        }


        File file = ResourceUtils.getFile("classpath:AverageMarksReport.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Create a data source from the reports list
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(courseAverageReportList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CreatedBy", "Lamia");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, pathToReports + "\\AverageMarksReport.pdf");
        return "Report generated: " + pathToReports + "\\AverageMarksReport.pdf";
    }

    /* Top Performance Students Report ------------------------------------------------------------- */
    public String generateTopPerformingStudentReport() throws FileNotFoundException, JRException {
        List<School> schoolList = schoolRepository.findAll();
        List<Mark> markList = markRepository.findAll();

        List<TopPerformingStudentsReport> topPerformingStudentsReportList = new ArrayList<>();

        for (School school : schoolList) {
            Mark topMark = null;
            for (Mark mark : markList) {
                if (mark.getStudent().getSchool().getId().equals(school.getId())) {
                    if (topMark == null || mark.getCourseMark() > topMark.getCourseMark()) {
                        topMark = mark;
                    }
                }
            }

            if (topMark != null) {
                Student topStudent = topMark.getStudent();
                TopPerformingStudentsReport report = TopPerformingStudentsReport.builder()
                        .schoolName(school.getName())
                        .studentName(topStudent.getName())
                        .rollNumber(topStudent.getRollNumber())
                        .build();
                topPerformingStudentsReportList.add(report);
            }
        }

        File file = ResourceUtils.getFile("classpath:TopPerformingStudentsReport.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Create a data source from the reports list
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(topPerformingStudentsReportList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CreatedBy", "Lamia");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, pathToReports + "\\TopPerformingStudentsReport.pdf");
        return "Report generated: " + pathToReports + "\\TopPerformingStudentsReport.pdf";
    }

    /* Student Performance Report ------------------------------------------------------------- */
    public String generateStudentPerformanceReport() throws FileNotFoundException, JRException {
        List<Student> studentList = studentRepository.findAll();
        List<Mark> markList = markRepository.findAll();

        Map<Long, List<Mark>> studentMarksMap = new HashMap<>();

        for (Mark mark : markList) {
            Long studentId = mark.getStudent().getId();
            List<Mark> marksForStudent = studentMarksMap.get(studentId);

            if (marksForStudent == null) {
                marksForStudent = new ArrayList<>();
                studentMarksMap.put(studentId, marksForStudent);
            }

            marksForStudent.add(mark);
        }

        List<StudentPerformanceReport> studentPerformanceReportList = new ArrayList<>(); // New list to hold reports

        for (Student student : studentList) {
            Long studentId = student.getId();
            List<Mark> marksForStudent = studentMarksMap.get(studentId);

            if (marksForStudent != null && !marksForStudent.isEmpty()) {
                Double totalMark = 0.0;
                int markCount = 0;

                for (Mark mark : marksForStudent) {
                    totalMark += mark.getCourseMark();
                    markCount++;
                }

                Double averageMark = totalMark / markCount;

                StudentPerformanceReport report = StudentPerformanceReport.builder()
                        .studentName(student.getName())
                        .rollNumber(student.getRollNumber())
                        .averageMark(averageMark)
                        .build();
                studentPerformanceReportList.add(report);
            }
        }


        File file = ResourceUtils.getFile("classpath:StudentOverallPerformance.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        // Create a data source from the reports list
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(studentPerformanceReportList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CreatedBy", "Lamia");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, pathToReports + "\\StudentPerformanceReport.pdf");
        return "Report generated: " + pathToReports + "\\StudentPerformanceReport.pdf";
    }

}
