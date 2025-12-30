package com.student.deep.service;


import com.student.deep.model.Student;
import com.student.deep.repository.StudentRepository;
import com.student.deep.exception.ResourceNotFoundException;
import com.student.deep.exception.DuplicateResourceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;

    public Student createStudent(Student student) {
        log.info("Creating student with email: {}", student.getEmail());

        if(studentRepository.existsByEmail(student.getEmail())) {
            throw new DuplicateResourceException("Student with email " + student.getEmail() + "already exists");
        }
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());

        if(student.getStatus()==null){
            student.setStatus("ACTIVE");
        }

        return studentRepository.save(student);
    }


    public List<Student> getAllStudents(){
        log.info("Fetching all students");
        return studentRepository.findAll();
    }

    public Student getStudentById(String id){
        log.info("Fetching all students with id: {}", id);
        return studentRepository.findById(id)
                                .orElseThrow(()-> new ResourceNotFoundException("Student not found"));

    }


    public Student getStudentByEmail(String email){
        log.info("Fetching student with email: {}", email);
        return studentRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("Student not found"));
    }

    public Student updateStudent(String id, Student studentDetails) {
        log.info("Updating student with id: {}", id);

        Student student=getStudentById(id);

        if(!student.getEmail().equals(studentDetails.getEmail())&& studentRepository.existsByEmail(studentDetails.getEmail())) {
            throw new DuplicateResourceException("Student with email " + studentDetails.getEmail() + "already exists");
        }

            student.setFirstName(studentDetails.getFirstName());
            student.setLastName(studentDetails.getLastName());
            student.setEmail(studentDetails.getEmail());
            student.setPhoneNumber(studentDetails.getPhoneNumber());
            student.setDepartment(studentDetails.getDepartment());
            student.setStatus(studentDetails.getStatus());
            student.setUpdatedAt(LocalDateTime.now());
            student.setYear(studentDetails.getYear());
            student.setGpa(studentDetails.getGpa());
            student.setAddress(studentDetails.getAddress());

            return studentRepository.save(student);


    }


    public void deleteStudent(String id) {
        log.info("Deleting student with id: {}", id);
        Student student=getStudentById(id);
        studentRepository.delete(student);
    }

    public List<Student>getStudentByDepartment(String department){
        log.info("Fetching all students by department: {}", department);
        return studentRepository.findByDepartment(department);
    }

    public List<Student>getStudentByStatus(String status){
        log.info("Fetching all students with status: {}", status);
        return studentRepository.findByStatus(status);
    }

    public List<Student>getStudentByyear(Integer year){
        log.info("Fetching all students by year: {}", year);
        return studentRepository.findByYear(year);
    }

    public List<Student>searchStudentByfirstName(String firstName){
        log.info("Fetching all students by first name: {}", firstName);
        return studentRepository.findByFirstNameContaining(firstName);
    }

    public List<Student>searchStudentBylastName(String lastName){
        log.info("Fetching all students by last name: {}", lastName);
        return studentRepository.findByLastNameContaining(lastName);
    }

    public List<Student> getStudentByMinimumGpa(Double gpa){
        log.info("Fetching all students by minimum gpa: {}", gpa);
        return studentRepository.findByGpaGreaterThanEqual(gpa);
    }



}
