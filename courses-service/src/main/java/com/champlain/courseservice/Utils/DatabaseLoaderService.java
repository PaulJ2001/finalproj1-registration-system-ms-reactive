package com.champlain.courseservice.Utils;

import com.champlain.courseservice.dataaccesslayer.Course;
import com.champlain.courseservice.dataaccesslayer.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class DatabaseLoaderService implements CommandLineRunner {


    @Autowired
    CourseRepository courseRepository;
    @Override
    public void run(String... args) throws Exception {

        Course course1 = Course.builder()
                .courseId(UUID.randomUUID().toString())
                .courseNumber("420-N45-LA")
                .courseName("Web Services and Distributed Computing")
                .numHours(60)
                .numCredits(2.00)
                .department("Computer Science")
                .build();

        Course course2 = Course.builder()
                .courseId(UUID.randomUUID().toString())
                .courseNumber("420-N53-LA")
                .courseName("Final Project 1")
                .numHours(105)
                .numCredits(2.67)
                .department("Computer Science")
                .build();

        Course course3 = Course.builder()
                .courseId(UUID.randomUUID().toString())
                .courseNumber("420-N52-LA")
                .courseName("Mobile Application Development 2 -IOS")
                .numHours(60)
                .numCredits(2.33)
                .department("Computer Science")
                .build();

        Course course4 = Course.builder()
                .courseId(UUID.randomUUID().toString())
                .courseNumber("420-N33-LA")
                .courseName(".NET Development")
                .numHours(60)
                .numCredits(2.00)
                .department("Computer Science")
                .build();

        Course course5 = Course.builder()
                .courseId(UUID.randomUUID().toString())
                .courseNumber("109-101-MQ")
                .courseName("Total Body Condition Fitness & Wellness")
                .numHours(30)
                .numCredits(1.00)
                .department("Physical Education")
                .build();

        Course course6 = Course.builder()
                .courseId(UUID.randomUUID().toString())
                .courseNumber("603-102-19")
                .courseName("The Genres of Literature")
                .numHours(60)
                .numCredits(2.00)
                .department("Physical Education")
                .build();


        Flux.just(course1, course2, course3, course4, course5, course6 )
                .flatMap(s -> courseRepository.insert(Mono.just(s))
                        .log(s.toString()))
                .subscribe();

    }
}
