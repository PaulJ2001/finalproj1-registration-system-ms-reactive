package com.champlain.courseservice.dataaccesslayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
class CourseRepositoryTest {

    @Autowired
    CourseRepository courseRepository;

    Course course1;
    Course course2;

    @BeforeEach
    public void setupDB(){
        course1 = buildCourse("Linux", "courseId_1");
        Publisher<Course> setup1 = courseRepository.deleteAll()
                .thenMany(courseRepository.save(course1));

        StepVerifier
                .create(setup1)
                .expectNextCount(1)
                .verifyComplete();

        course2 = buildCourse("Game Development", "courseId_2");
        Publisher<Course> setup2 = courseRepository.deleteAll()
                .thenMany(courseRepository.save(course2));

        StepVerifier
                .create(setup2)
                .expectNextCount(1)
                .verifyComplete();

    }

    @Test
    public void shouldSaveSingleCourse(){
        //arrange
        Course newCourse = buildCourse("Project Management", "courseId_3");
        Publisher<Course> setup = courseRepository.save(newCourse);

        //act and assert
        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test public void shouldGetTwoCourse(){


        StepVerifier
                .create(courseRepository.findAll())
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test public void findCourseByCourseId_shouldFindOne(){
        StepVerifier
                .create(courseRepository.findCourseByCourseId(course2.getCourseId()))
                .assertNext(course -> {
                    assertThat(course.getCourseId()).isEqualTo(course2.getCourseId());
                    assertThat(course.getCourseName()).isEqualTo(course2.getCourseName());

                });
    }


    private Course buildCourse(String courseName, String courseId){
        return Course.builder()
                .courseId(courseId)
                .courseNumber("420B02")
                .courseName("Java")
                .numHours(2)
                .numCredits(3.1)
                .department("Computer Science")
                .build();
    }
}
