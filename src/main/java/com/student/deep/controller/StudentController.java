package com.student.deep.controller;


import com.student.deep.model.Student;
import com.student.deep.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<?> createStudent(@Valid @RequestBody Student student) {

        Student createdStudent=studentService.createStudent(student);
        return new ResponseEntity<>(createdStudent, HttpStatus.OK);
    }


    @GetMapping
    private ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students=studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable String id) {
        Student student=studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Student> getStudentByEmail(@PathVariable String email) {
        Student student=studentService.getStudentByEmail(email);
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Student> updateStudent(@PathVariable String id, @Valid @RequestBody Student studentDetails) {
        Student updatedStudent=studentService.updateStudent(id,studentDetails);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> deleteStudent(@PathVariable String id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/department/{department}")
    public ResponseEntity<List<Student>> getStudentByDepartment(@PathVariable String department) {
        List<Student>students=studentService.getStudentByDepartment(department);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Student>> getStudentBYStatus(@PathVariable String status) {
        List<Student>students=studentService.getStudentByStatus(status);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/search/firstName/{firstName}")
    public ResponseEntity<List<Student>> searchByfirstName(@PathVariable String firstName) {
        List<Student>students=studentService.searchStudentByfirstName(firstName);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/search/lastName/{lastName}")
    public ResponseEntity<List<Student>> searchBylastName(@PathVariable String lastName) {
        List<Student>students=studentService.searchStudentBylastName(lastName);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/year/{year}")
    public ResponseEntity<List<Student>> getStudentsByyear(@PathVariable Integer year) {
        List<Student>students=studentService.getStudentByyear(year);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/gpa/{minGpa}")
    public ResponseEntity<List<Student>> getStudentsByMinimumGpa(@PathVariable Double minGpa) {
        List<Student>students=studentService.getStudentByMinimumGpa(minGpa);
        return ResponseEntity.ok(students);
    }




}
