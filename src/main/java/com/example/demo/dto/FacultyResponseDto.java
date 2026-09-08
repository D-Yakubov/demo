package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class FacultyResponseDto {
    private Long id;
    private String facultyName;
    private List<StudentResponseDto> students;
}
