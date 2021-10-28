package com.dailycodebuffer.spring.data.jpa.tutorial.repository;

import com.dailycodebuffer.spring.data.jpa.tutorial.entity.Course;
import com.dailycodebuffer.spring.data.jpa.tutorial.entity.Student;
import com.dailycodebuffer.spring.data.jpa.tutorial.entity.Teacher;
import net.bytebuddy.TypeCache;
import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Test
    public void printCourses(){
        List<Course> courses = courseRepository.findAll();
        System.out.println("courses : " + courses);
    }

    @Test
    public void saveCourseWithTeacher(){

        Teacher teacher = Teacher.builder()
                .firstName("Clemens")
                .lastName("Kerber")
                .build();

        Course course = Course.builder()
                .titel("Python")
                .credit(6)
                .teacher(teacher)
                .build();

        courseRepository.save(course);
    }

    @Test
    public void findAllPagination(){
        Pageable firstPageWithThreeRecords =
                PageRequest.of(0,3);
        Pageable secoundPageWithTwoRecords =
                PageRequest.of(1,2);
        List<Course> courses =
                courseRepository.findAll(secoundPageWithTwoRecords).getContent();

        Long totalElements =
                courseRepository.findAll(secoundPageWithTwoRecords).getTotalElements();

        long totalPages =
                courseRepository.findAll(secoundPageWithTwoRecords).getTotalPages();

        System.out.println("totalPages = " + totalPages);
        System.out.println("totalElements = " + totalElements);
        System.out.println("courses = " + courses);
    }

    @Test
    public void findAllSorting(){
        Pageable sortByTitle =
                PageRequest.of(
                        0,
                        2,
                        Sort.by("titel")
                );

        Pageable sortByCreditDesc =
                PageRequest.of(
                        0,
                        2,
                        Sort.by("credit").descending());

        Pageable sortByTitleAndCreditDesc =
                PageRequest.of(0,2, Sort.by("titel")
                        .descending().and(Sort.by("credit")));

        List<Course> courses = courseRepository.findAll(sortByTitle).getContent();

        System.out.println("courses = " + courses);
    }

    @Test
    public void printfindByTitelContaining(){
        Pageable firstPageTenRecords =
                PageRequest.of(0,10);

        List<Course> courses = courseRepository.findByTitelContaining(
                "D", firstPageTenRecords).getContent();

        System.out.println("courses = " + courses);
    }

    @Test
    public void saveCourseWithStudentAndTeacher(){

        Teacher teacher = Teacher.builder()
                .lastName("Scherl")
                .firstName("Hansi")
                .build();

        Student student = Student.builder()
                .firstName("Anna")
                .lastName("Kerber")
                .emailId("ankerber@tsn.at")
                .build();

        Course course = Course.builder()
                .titel("Ai")
                .credit(12)
                .teacher(teacher)
                .build();

        course.addStudents(student);

        courseRepository.save(course);
    }
}