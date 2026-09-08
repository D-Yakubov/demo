package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Faculty;
import com.example.demo.model.Student;
import com.example.demo.repository.StudentRepository;
import com.example.demo.repository.FacultyRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;


    public StudentService(StudentRepository studentRepository, FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student updateStudent(Long id, Student studentDetails) {
        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
        existingStudent.setName(studentDetails.getName());
        existingStudent.setSurname(studentDetails.getSurname());
        existingStudent.setBirthDate(studentDetails.getBirthDate());
        existingStudent.setAddress(studentDetails.getAddress());
        existingStudent.setAcademicYear(studentDetails.getAcademicYear());
        existingStudent.setFaculty(studentDetails.getFaculty());

        return studentRepository.save(existingStudent);
    }


    public Student saveStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student getStudentById(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Student not found with id: " + id));
    }

    /*public List<Student> findAll() {
        return studentRepository.findAll();
    }*/

    public void deleteStudentById(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new ResourceNotFoundException("Cannot delete. Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
    }

    public List<Student> getStudentsByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name);
    }

    public Student assignFacultyToStudent(Long studentId, Long facultyId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        student.setFaculty(faculty);
        return studentRepository.save(student);

    }

}
