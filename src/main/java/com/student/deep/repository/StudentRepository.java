package com.student.deep.repository;

import com.student.deep.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository  extends MongoRepository<Student,String> {

    Optional<Student> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Student>findByDepartment(String department);

    List<Student>findByStatus(String status);

    List<Student> findByYear(Integer year);

    @Query("{'firstName': {$regex: ?0, $options: 'i'}}")
    List<Student> findByFirstNameContaining(String firstName);

    @Query("{'lastName': {$regex: ?0, $options: 'i'}}")
    List<Student> findByLastNameContaining(String lastName);

    @Query("{'gpa': {$gte: ?0}}")
    List<Student> findByGpaGreaterThanEqual(Double gpa);


}
