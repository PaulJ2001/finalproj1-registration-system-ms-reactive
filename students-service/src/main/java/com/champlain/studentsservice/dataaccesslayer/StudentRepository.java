package com.champlain.studentsservice.dataaccesslayer;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface StudentRepository extends ReactiveMongoRepository<Student, String> {


    public Mono<Student> findStudentByStudentId (String studentId);







}
