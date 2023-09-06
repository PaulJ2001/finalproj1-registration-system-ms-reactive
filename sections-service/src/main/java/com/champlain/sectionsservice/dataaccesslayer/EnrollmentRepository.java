package com.champlain.sectionsservice.dataaccesslayer;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EnrollmentRepository extends ReactiveCrudRepository<Enrollment, Integer> {

    public Mono<Enrollment> getEnrollmentByEnrollmentId (String enrollmentId);

    public Flux<Enrollment> findEnrollmentByStudentId (String studentId);

    public Flux<Enrollment> findEnrollmentByCourseId (String courseId);

    public Flux<Enrollment> findEnrollmentByEnrollmentYear (Integer enrollmentYear);

    public Flux<Enrollment> findEnrollmentByStudentIdAndCourseId (String studentId, String courseId);

    public Flux<Enrollment> findEnrollmentByStudentIdAndEnrollmentYear (String studentId, Integer enrollmentYear);

    public Flux<Enrollment> findEnrollmentByCourseIdAndEnrollmentYear (String studentId, Integer enrollmentYear);

    public Flux<Enrollment> findEnrollmentByStudentIdAndCourseIdAndEnrollmentYear (String studentId, String courseId, Integer enrollmentYear);


}
