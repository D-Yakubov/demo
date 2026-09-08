package com.example.demo.controller;

import com.example.demo.dto.FacultyResponseDto;
import com.example.demo.model.Faculty;
import com.example.demo.service.FacultyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/faculties")
public class FacultyController {

    private final FacultyService facultyService;

    @Value("${api.security.secret-token}")
    private String validToken;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    private boolean isNotAuthorized(String token) {
        return token == null || !token.equals(validToken);
    }

    @PostMapping
    public ResponseEntity<?> createFaculty(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody Faculty faculty) {

        if (isNotAuthorized(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid or missing token");
        }

        Faculty savedFaculty = facultyService.saveFaculty(faculty);
        return new ResponseEntity<>(savedFaculty, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFacultyWithStudents(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable("id") Long id) {

        if (isNotAuthorized(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid or missing token");
        }

        FacultyResponseDto facultyDto = facultyService.getFacultyWithStudents(id);
        return ResponseEntity.ok(facultyDto);
    }
}
