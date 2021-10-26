package com.dailycodebuffer.spring.data.jpa.tutorial.repository;

import com.dailycodebuffer.spring.data.jpa.tutorial.entity.Guardian;
import com.dailycodebuffer.spring.data.jpa.tutorial.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;


@SpringBootTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository studentRepository;

    @Test
    public void saveStudent(){
        Student student = Student.builder()
                .studentId(1L)
                .emailId("clekerber@gmail.com")
                .firstName("Clemens")
                .lastName("Kerber")
                //.guardianName("Hansi")
                //.guardianEmail("hansi@gmail.com")
               // .guardianMobile("99999999999")
                .build();
        studentRepository.save(student);
    }

    @Test
    public void saveStudentWithGuardian(){

        Guardian guardian = Guardian.builder()
                .email("hansi@gmail.com")
                .mobile("99999994599")
                .name("Hansi")
                .build();

        Student student = Student.builder()
                .firstName("Johannes")
                .emailId("johannes@gmail.com")
                .lastName("Kerber")
                .guardian(guardian)
                .build();

        studentRepository.save(student);
    }

    @Test
    public void printAllStudent(){
        List<Student> studentList = studentRepository.findAll();
        System.out.println("studentList = " + studentList);
    }
}