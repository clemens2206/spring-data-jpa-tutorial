package com.dailycodebuffer.spring.data.jpa.tutorial.repository;

import com.dailycodebuffer.spring.data.jpa.tutorial.entity.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


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
                .guardianName("Hansi")
                .guardianEmail("hansi@gmail.com")
                .guardianMobile("99999999999")
                .build();

        studentRepository.save(student);
    }
}