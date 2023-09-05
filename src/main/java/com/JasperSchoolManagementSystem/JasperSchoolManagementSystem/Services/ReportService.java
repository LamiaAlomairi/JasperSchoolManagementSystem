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

    /* School Students Report ------------------------------------------------------------- */
    public String generateSchoolStudentsReport() throws FileNotFoundException, JRException {
        List<School> schoolList = schoolRepository.findAll();
        List<Student> studentList = studentRepository.findAll();

        Map<Long, Integer> schoolStudentCountMap = new HashMap<>();

        for (School school : schoolList) {
            int studentCount = 0;
            for (Student student : studentList) {
                if (student.getSchool().getId().equals(school.getId())) {
                    studentCount++;
                }
            }
            schoolStudentCountMap.put(school.getId(), studentCount);
        }

        List<SchoolStudentsReport> schoolStudentsReportList = new ArrayList<>();

        for (School school : schoolList) {
            Integer studentCount = schoolStudentCountMap.get(school.getId());
            if (studentCount != null) {
                SchoolStudentsReport report = SchoolStudentsReport.builder()
                        .schoolName(school.getName())
                        .totalStudents(studentCount)
                        .build();
                schoolStudentsReportList.add(report);
            }
        }

        File file = ResourceUtils.getFile("classpath:SchoolStudentsReport.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(schoolStudentsReportList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CreatedBy", "Lamia");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, pathToReports + "\\SchoolStudentsReport.pdf");
        return "Report generated: " + pathToReports + "\\SchoolStudentsReport.pdf";
    }

    /* School Course Performance Report ------------------------------------------------------------- */
    public String generateSchoolCoursePerformanceReport() throws FileNotFoundException, JRException {
        List<School> schoolList = schoolRepository.findAll();
        List<Course> courseList = courseRepository.findAll();
        List<Mark> markList = markRepository.findAll();

        Map<Long, Map<Long, List<Double>>> schoolCourseMarksMap = new HashMap<>();

        // Iterate through the marks
        for (Mark mark : markList) {
            Long schoolId = mark.getStudent().getSchool().getId();
            Long courseId = mark.getCourse().getId();
            Double courseMark = mark.getCourseMark();

            // Get or create the school's map
            Map<Long, List<Double>> schoolMap = schoolCourseMarksMap.computeIfAbsent(schoolId, k -> new HashMap<>());

            // Get or create the course's list of marks
            List<Double> courseMarks = schoolMap.computeIfAbsent(courseId, k -> new ArrayList<>());

            // Add the course mark to the list
            courseMarks.add(courseMark);
        }

        File file = ResourceUtils.getFile("classpath:SchoolCoursePerformanceReport.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        List<SchoolCoursePerformanceReport> schoolCoursePerformanceReportList = new ArrayList<>();

        // Iterate through the schools
        for (School school : schoolList) {
            // Get the map of course marks for the school
            Map<Long, List<Double>> courseMarksMap = schoolCourseMarksMap.get(school.getId());

            // Check if the map is not null
            if (courseMarksMap != null) {
                double highestAverageMark = 0.0;
                Course highestAverageMarkCourse = null;

                // Iterate through the courses
                for (Course course : courseList) {
                    // Get the list of marks for the course
                    List<Double> marksForCourse = courseMarksMap.get(course.getId());

                    // Check if the list is not null and not empty
                    if (marksForCourse != null && !marksForCourse.isEmpty()) {
                        double totalMarks = 0.0;
                        int numMarks = 0;

                        // Calculate the total marks and count the number of marks
                        for (Double mark : marksForCourse) {
                            totalMarks += mark;
                            numMarks++;
                        }

                        // Calculate the average mark
                        double averageMark;

                        if (numMarks > 0) {
                            averageMark = totalMarks / numMarks;
                        } else {
                            averageMark = 0.0;
                        }

                        // Check if this course has a higher average mark
                        if (averageMark > highestAverageMark) {
                            highestAverageMark = averageMark;
                            highestAverageMarkCourse = course;
                        }
                    }
                }

                // Create a performance report for the course with the highest average mark
                if (highestAverageMarkCourse != null) {
                    SchoolCoursePerformanceReport report = SchoolCoursePerformanceReport.builder()
                            .schoolName(school.getName())
                            .courseName(highestAverageMarkCourse.getName())
                            .averageMark(highestAverageMark)
                            .build();
                    schoolCoursePerformanceReportList.add(report);
                }
            }
        }

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(schoolCoursePerformanceReportList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CreatedBy", "Lamia");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, pathToReports + "\\SchoolCoursePerformanceReport.pdf");
        return "Report generated: " + pathToReports + "\\SchoolCoursePerformanceReport.pdf";
    }

    /* School Performance Report ------------------------------------------------------------- */
    public String generateSchoolPerformanceReport() throws FileNotFoundException, JRException {
        List<School> schoolList = schoolRepository.findAll();
        List<SchoolPerformanceReport> schoolPerformanceReportList = new ArrayList<>();

        for (School school : schoolList) {
            List<Student> studentsInSchool = school.getStudents(); // Assuming you have a relationship set up in the School entity

            if (!studentsInSchool.isEmpty()) {
                double totalMarks = 0.0;
                int totalStudents = studentsInSchool.size();

                for (Student student : studentsInSchool) {
                    for (Mark mark : student.getMarks()) {
                        totalMarks += mark.getCourseMark();
                    }
                }

                double averageMark = (totalStudents > 0) ? (totalMarks / totalStudents) : 0.0;

                SchoolPerformanceReport schoolPerformanceReport = SchoolPerformanceReport.builder()
                        .schoolName(school.getName())
                        .averageMark(averageMark)
                        .build();

                schoolPerformanceReportList.add(schoolPerformanceReport);
            }
        }

        File file = ResourceUtils.getFile("classpath:SchoolPerformanceReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(schoolPerformanceReportList);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CreatedBy", "Lamia");

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToPdfFile(jasperPrint, pathToReports + "\\SchoolPerformanceReport.pdf");

        return "Report generated: " + pathToReports + "\\SchoolPerformanceReport.pdf";
    }

}
