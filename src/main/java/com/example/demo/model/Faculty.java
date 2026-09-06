package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "faculties")
public class Faculty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "faculty_name")
    private String facultyName;

    public Faculty() {

    }

    // initializing constructor
    public Faculty(Long id, String facultyName) {
        this.id = id;
        this.facultyName = facultyName;
    }

    // initializing getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFacultyName() {
        return facultyName;
    }

    public void setFacultyName(String facultyName) {
        this.facultyName = facultyName;
    }
}
