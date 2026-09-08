package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.service.StudentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Value("${api.security.secret-token}")
    private String validToken;

    private  boolean isNotAuthorized(String token) {
        return token == null || !token.equals(validToken);
    }

    // 1. Talaba id si orqali talaba malumotlarini olish
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudentById(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable("id") Long id) {

        if (isNotAuthorized(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid or missing token");
        }
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    // 2. Talaba id si orqali talaba malumotlarini ozgartirish. Bu usulda talaba malumotlarining
    /*hamma field'larini o'zgartirishimizga to'g'ri keladi agar qaysidir field'ga qiymat bermasak
    avtomatik tarzda uni null ga o'zgartirib qo'yadi*/
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudentById(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable("id") Long id,
            @RequestBody Student student) {

        if (isNotAuthorized(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid or missing token");
        }

        Student updatedStudent = studentService.updateStudent(id, student);
        return ResponseEntity.ok(updatedStudent);
    }

    // 2. Talaba ma'lumotlarining partial qismini o'zgartirish uchun PUT methodidan emas PATCH
    // methodidan foydalanamiz. Bunda faqat berilgan field'lar ozgaradi va boshqalari oz holida saqlanadi
    @PatchMapping("/{id}")
    public ResponseEntity<?> patchStudentById(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable("id") Long id,
            @RequestBody Student partialData) { //

        if (isNotAuthorized(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid or missing token");
        }

        //Avvalo malumotlarini o'zgartirmoqchi bo'lgan studentimizning barcha malumotlarini databazamizdan olamiz
        Student currentStudent = studentService.getStudentById(id);

        if (partialData.getName() != null) currentStudent.setName(partialData.getName());
        if (partialData.getSurname() != null) currentStudent.setSurname(partialData.getSurname());
        if (partialData.getBirthDate() != null) currentStudent.setBirthDate(partialData.getBirthDate());
        if (partialData.getAddress() != null) currentStudent.setAddress(partialData.getAddress());
        if (partialData.getAcademicYear() != null) currentStudent.setAcademicYear(partialData.getAcademicYear());
        if (partialData.getFaculty() != null) currentStudent.setFaculty(partialData.getFaculty());

        Student updatedStudent = studentService.updateStudent(id, currentStudent);

        return ResponseEntity.ok(updatedStudent);
    }

    // 3. Yangi talaba kiritish
    @PostMapping
    public ResponseEntity<?> createStudent(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody Student student) {

        if (isNotAuthorized(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid or missing token");
        }

        Student savedStudent = studentService.saveStudent(student);
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    // 4. Talaba id si orqali talabani o'chirish
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudentById(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable("id") Long id) {

        if (isNotAuthorized(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid or missing token");
        }

        studentService.deleteStudentById(id);
        return ResponseEntity.ok("Student deleted successfully.");
    }

    // 5. Name orqali talabalar ro'yxatini olish (Yozuv registri hisobga olinmasin)
    @GetMapping("/search")
    public ResponseEntity<?> searchStudentsByName(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestParam("name") String name) {

        if (isNotAuthorized(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid or missing token");
        }

        List<Student> studentList = studentService.getStudentsByName(name);
        return ResponseEntity.ok(studentList);
    }

    // Talabalarni ma'lum fakultetga biriktirishimiz uchun assignFaculty nomli endpoint ishlab chiqamiz:
    @PutMapping("/{studentId}/assign-faculty/{facultyId}")
    public ResponseEntity<?> assignFaculty(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long studentId,
            @PathVariable Long facultyId) {

        if (isNotAuthorized(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid or missing token");
        }

        Student updatedStudent = studentService.assignFacultyToStudent(studentId, facultyId);
        return ResponseEntity.ok(updatedStudent);
    }

}
