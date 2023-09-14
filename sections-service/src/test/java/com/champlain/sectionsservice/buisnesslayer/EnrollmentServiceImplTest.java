package com.champlain.sectionsservice.buisnesslayer;

import com.champlain.sectionsservice.dataaccesslayer.Enrollment;
import com.champlain.sectionsservice.dataaccesslayer.EnrollmentRepository;
import com.champlain.sectionsservice.domainclientlayer.CourseClient;
import com.champlain.sectionsservice.domainclientlayer.StudentClient;
import com.champlain.sectionsservice.presentationlayer.EnrollmentRequestDTO;
import com.champlain.sectionsservice.presentationlayer.EnrollmentResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class EnrollmentServiceImplTest {

    @Autowired
    EnrollmentService enrollmentService;

    @MockBean
    EnrollmentRepository enrollmentRepository;

    @MockBean
    StudentClient studentClient;

    @MockBean
    CourseClient courseClient;

    @Test
    void getAllByStudentId() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("studentId", "student123");
        queryParams.put("courseId", null);
        queryParams.put("enrollYear", null);

        when(enrollmentRepository.findEnrollmentByStudentId(anyString()))
                .thenReturn(Flux.just(buildEnrollment()));
        Flux<EnrollmentResponseDTO> result = enrollmentService.getAllEnrollments(queryParams);

        StepVerifier
                .create(result)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getAllByCourseId() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("studentId", null);
        queryParams.put("courseId", "course123");
        queryParams.put("enrollYear", null);

        when(enrollmentRepository.findEnrollmentByCourseId(anyString()))
                .thenReturn(Flux.just(buildEnrollment()));

        Flux<EnrollmentResponseDTO> result = enrollmentService.getAllEnrollments(queryParams);

        StepVerifier
                .create(result)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getAllByEnrollmentYear() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("studentId", null);
        queryParams.put("courseId", null);
        queryParams.put("enrollYear", "2023");

        when(enrollmentRepository.findEnrollmentByEnrollmentYear(anyInt()))
                .thenReturn(Flux.just(buildEnrollment()));
        Flux<EnrollmentResponseDTO> result = enrollmentService.getAllEnrollments(queryParams);

        StepVerifier
                .create(result)
                .expectNextCount(1)
                .verifyComplete();
    }


    @Test
    void getAllByStudentIdAndCourseIdAndEnrollmentYear() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("studentId", "student123");
        queryParams.put("courseId", "course123");
        queryParams.put("enrollYear", "2023");

        when(enrollmentRepository.findEnrollmentByStudentIdAndCourseIdAndEnrollmentYear(anyString(), anyString(), anyInt()))
                .thenReturn(Flux.just(buildEnrollment()));
        Flux<EnrollmentResponseDTO> result = enrollmentService.getAllEnrollments(queryParams);

        StepVerifier
                .create(result)
                .expectNextCount(1)
                .verifyComplete();
    }


    @Test
    void getAllByStudentIdAndCourseId() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("studentId", "student123");
        queryParams.put("courseId", "course123");
        queryParams.put("enrollYear", null);

        when(enrollmentRepository.findEnrollmentByStudentIdAndCourseId(anyString(), anyString()))
                .thenReturn(Flux.just(buildEnrollment()));
        Flux<EnrollmentResponseDTO> result = enrollmentService.getAllEnrollments(queryParams);

        StepVerifier
                .create(result)
                .expectNextCount(1)
                .verifyComplete();
    }


    @Test
    void getAllByStudentIdAndEnrollmentYear() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("studentId", "student123");
        queryParams.put("courseId", null);
        queryParams.put("enrollYear", "2023");

        when(enrollmentRepository.findEnrollmentByStudentIdAndEnrollmentYear(anyString(), anyInt()))
                .thenReturn(Flux.just(buildEnrollment()));
        Flux<EnrollmentResponseDTO> result = enrollmentService.getAllEnrollments(queryParams);

        StepVerifier
                .create(result)
                .expectNextCount(1)
                .verifyComplete();
    }


    @Test
    void getAllByCourseIdAndEnrollmentYear() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("studentId", null);
        queryParams.put("courseId", "course123");
        queryParams.put("enrollYear", "2023");

        when(enrollmentRepository.findEnrollmentByCourseIdAndEnrollmentYear(anyString(), anyInt()))
                .thenReturn(Flux.just(buildEnrollment()));
        Flux<EnrollmentResponseDTO> result = enrollmentService.getAllEnrollments(queryParams);

        StepVerifier
                .create(result)
                .expectNextCount(1)
                .verifyComplete();
    }


    @Test
    void getAllEnrollments() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("studentId", null);
        queryParams.put("courseId", null);
        queryParams.put("enrollYear", null);

        when(enrollmentRepository.findAll())
                .thenReturn(Flux.just(buildEnrollment()));
        Flux<EnrollmentResponseDTO> result = enrollmentService.getAllEnrollments(queryParams);

        StepVerifier
                .create(result)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void invalidEnrollmentYearInput() {
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("studentId", null);
        queryParams.put("courseId", null);
        queryParams.put("enrollYear", "letter");

        Flux<EnrollmentResponseDTO> result = enrollmentService.getAllEnrollments(queryParams);

        StepVerifier
                .create(result)
                .expectError();

    }

    @Test
    void shouldGetEnrollmentByEnrollmentId() {
        //arrange
        Enrollment enrollment = buildEnrollment();
        String ENROLLMENT_ID = enrollment.getEnrollmentId();

        when(enrollmentRepository.getEnrollmentByEnrollmentId(ENROLLMENT_ID)).thenReturn(Mono.just(enrollment));

        //act
        Mono<EnrollmentResponseDTO> enrollmentResponseDTOMono = enrollmentService.getEnrollmentByEnrollmentId(ENROLLMENT_ID);

        //assert
        StepVerifier
                .create(enrollmentResponseDTOMono)
                .consumeNextWith(foundEnrollment -> {
                    assertEquals(enrollment.getEnrollmentId(), foundEnrollment.getEnrollmentId());
                    assertEquals(enrollment.getEnrollmentYear(), foundEnrollment.getEnrollmentYear());
                    assertEquals(enrollment.getSemester(), foundEnrollment.getSemester());
                    assertEquals(enrollment.getStudentId(), foundEnrollment.getStudentId());
                    assertEquals(enrollment.getStudentFirstName(), foundEnrollment.getStudentFirstName());
                    assertEquals(enrollment.getStudentLastName(), foundEnrollment.getStudentLastName());
                    assertEquals(enrollment.getCourseId(), foundEnrollment.getCourseId());
                    assertEquals(enrollment.getCourseName(), foundEnrollment.getCourseName());
                    assertEquals(enrollment.getCourseNumber(), foundEnrollment.getCourseNumber());
                })
                .verifyComplete();
    }

    @Test
    void shouldUpdateEnrollmentByEnrollmentId() {
        //arrange
        Enrollment enrollment = buildEnrollment();
        String ENROLLMENT_ID = enrollment.getEnrollmentId();

        when(enrollmentRepository.getEnrollmentByEnrollmentId(ENROLLMENT_ID)).thenReturn(Mono.just(enrollment));
        when(studentClient.getStudentByStudentId(enrollment.getStudentId())).thenReturn(Mono.just(StudentResponseDTO.builder().build()));
        when(courseClient.getCourseByCourseId(enrollment.getCourseId())).thenReturn(Mono.just(CourseResponseDTO.builder().build()));
        when(enrollmentRepository.save(any())).thenReturn(Mono.just(enrollment));

        //act
        Mono<EnrollmentResponseDTO> enrollmentResponseDTOMono = enrollmentService.updateEnrollment(ENROLLMENT_ID, Mono.just(EnrollmentRequestDTO.builder().build()));

        //assert
        StepVerifier
                .create(enrollmentResponseDTOMono)
                .consumeNextWith(foundEnrollment -> {
                    assertEquals(enrollment.getEnrollmentId(), foundEnrollment.getEnrollmentId());
                    assertEquals(enrollment.getEnrollmentYear(), foundEnrollment.getEnrollmentYear());
                    assertEquals(enrollment.getSemester(), foundEnrollment.getSemester());
                    assertEquals(enrollment.getStudentId(), foundEnrollment.getStudentId());
                    assertEquals(enrollment.getStudentFirstName(), foundEnrollment.getStudentFirstName());
                    assertEquals(enrollment.getStudentLastName(), foundEnrollment.getStudentLastName());
                    assertEquals(enrollment.getCourseId(), foundEnrollment.getCourseId());
                    assertEquals(enrollment.getCourseName(), foundEnrollment.getCourseName());
                    assertEquals(enrollment.getCourseNumber(), foundEnrollment.getCourseNumber());
                })
                .verifyComplete();
    }

    @Test
    void shouldDeleteEnrollment() {
        //arrange
        Enrollment enrollment = buildEnrollment();
        String ENROLLMENT_ID = enrollment.getEnrollmentId();

        when(enrollmentRepository.delete(any())).thenReturn(Mono.empty());
        when(enrollmentRepository.getEnrollmentByEnrollmentId(ENROLLMENT_ID)).thenReturn(Mono.just(enrollment));

        //act
        Mono<Void> enrollmentResponseDTOMono = enrollmentService.deleteEnrollment(ENROLLMENT_ID);

        //assert
        StepVerifier
                .create(enrollmentResponseDTOMono)
                .expectNextCount(0)
                .verifyComplete();


    }

    private Enrollment buildEnrollment() {
        return  new Enrollment();
    }

}