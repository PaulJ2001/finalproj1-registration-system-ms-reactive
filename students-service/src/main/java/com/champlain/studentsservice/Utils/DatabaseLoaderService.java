package com.champlain.studentsservice.Utils;

import com.champlain.studentsservice.dataaccesslayer.Student;
import com.champlain.studentsservice.dataaccesslayer.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class DatabaseLoaderService implements CommandLineRunner {

    @Autowired
    StudentRepository studentRepository;

    @Override
    public void run(String... args) throws Exception {

        Student student1 = Student.builder()
                .firstName("Sam")
                .lastName("Jones")
                .studentId(UUID.randomUUID().toString())
                .program("Computer Science")
                .build();

        Student student2 = Student.builder()
                .firstName("Ed")
                .lastName("Leroy")
                .studentId(UUID.randomUUID().toString())
                .program("Commerce")
                .build();

        Student student3 = Student.builder()
                .firstName("Joe")
                .lastName("Rose")
                .studentId(UUID.randomUUID().toString())
                .program("Medicine")
                .build();

        Student student4 = Student.builder()
                .firstName("Atlas")
                .lastName("Ye")
                .studentId(UUID.randomUUID().toString())
                .program("Drama")
                .build();


        Flux.just(student1, student2, student3, student4 )
                .flatMap(s -> studentRepository.insert(Mono.just(s))
                .log(s.toString()))
                .subscribe();
    }
}
