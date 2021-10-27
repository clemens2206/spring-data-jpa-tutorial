package com.dailycodebuffer.spring.data.jpa.tutorial.repository;

import com.dailycodebuffer.spring.data.jpa.tutorial.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    public List<Student> findByFirstName(String firstName);

    public List<Student> findByFirstNameContaining(String name);

    public List<Student> findByLastNameNotNull();

    public List<Student> findByGuardianName(String GuardianName);

    public Student findByFirstNameAndLastName(String firstName, String lastName);

    //JPQL
    @Query("select s from Student s where s.emailId = ?1")
    public Student getStudentByEmailId(String email);

    //JPQL
    @Query("select s.firstName from Student s where s.emailId = ?1")
    public String getStudentFirstNameByEmailId(String email);

    //Native
    @Query(
            value = "SELECT * FROM tbl_student s where s.email_address = ?1 ",
            nativeQuery = true)
    public Student getStudentByEmailIdNative(String email);

    //Native Named Param
    @Query(
            value = "SELECT * FROM tbl_student s where s.email_address = :emailId ",
            nativeQuery = true)
    public Student getStudentByEmailIdNativeNamedParam(@Param("emailId") String  emailId);
}
