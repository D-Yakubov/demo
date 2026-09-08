package com.example.demo.service;

import com.example.demo.dto.FacultyResponseDto;
import com.example.demo.dto.StudentResponseDto;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Faculty;
import com.example.demo.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyService {

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty saveFaculty(Faculty faculty) {
        return facultyRepository.save(faculty);
    }

    public FacultyResponseDto getFacultyWithStudents(Long id) {

        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Faculty not found with id: "+id));

        List<StudentResponseDto> studentDtos = faculty.getStudents().stream().map(student -> {
            StudentResponseDto dto = new StudentResponseDto();
            dto.setId(student.getId());
            dto.setName(student.getName());
            dto.setSurname(student.getSurname());
            dto.setBirthDate(student.getBirthDate());
            dto.setAddress(student.getAddress());
            dto.setAcademicYear(student.getAcademicYear());
            return dto;
        }).collect(Collectors.toList());

        FacultyResponseDto facultyDto = new FacultyResponseDto();
        facultyDto.setId(faculty.getId());
        facultyDto.setFacultyName(faculty.getFacultyName());
        facultyDto.setStudents(studentDtos);

        return facultyDto;

    }
}
