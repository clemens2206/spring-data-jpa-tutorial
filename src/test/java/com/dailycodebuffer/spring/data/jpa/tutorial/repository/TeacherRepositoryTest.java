package com.dailycodebuffer.spring.data.jpa.tutorial.repository;

import com.dailycodebuffer.spring.data.jpa.tutorial.entity.Course;
import com.dailycodebuffer.spring.data.jpa.tutorial.entity.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TeacherRepositoryTest {

    @Autowired
    private TeacherRepository teacherRepository;


    @Test
    public void saveTeacher(){

        Course courseDBA = Course.builder()
                .titel("DBA")
                .credit(5)
                .build();


        Course courseJava = Course.builder()
                .titel("Java")
                .credit(6)
                .build();

        Teacher teacher = Teacher.builder()
                .firstName("Qutob")
                .lastName("Kahn")
                .courses(List.of(courseDBA, courseJava))
                .build();

        teacherRepository.save(teacher);
    }
}