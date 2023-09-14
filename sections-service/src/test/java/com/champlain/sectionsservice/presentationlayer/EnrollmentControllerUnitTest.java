package com.champlain.sectionsservice.presentationlayer;

import com.champlain.sectionsservice.buisnesslayer.CourseResponseDTO;
import com.champlain.sectionsservice.buisnesslayer.EnrollmentService;
import com.champlain.sectionsservice.buisnesslayer.StudentResponseDTO;
import com.champlain.sectionsservice.dataaccesslayer.Semester;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@AutoConfigureWebTestClient
class EnrollmentControllerUnitTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    EnrollmentController enrollmentController;

    @MockBean
    EnrollmentService enrollmentService;


    StudentResponseDTO studentResponseDTO = StudentResponseDTO.builder()
            .firstName("Donna")
            .lastName("Hornsby")
            .studentId("student123")
            .program("History")
            .build();

    CourseResponseDTO courseResponseDTO = CourseResponseDTO.builder()
            .courseId("course123")
            .courseName("Web Services")
            .courseNumber("420-N45-LA")
            .department("Computer Science")
            .numCredits(2.0)
            .numHours(60)
            .build();

    EnrollmentRequestDTO enrollmentRequestDTO = EnrollmentRequestDTO.builder()
            .enrollmentYear(2023)
            .semester(Semester.FALL)
            .studentId(studentResponseDTO.getStudentId())
            .courseId(courseResponseDTO.getCourseId())
            .build();
    EnrollmentResponseDTO enrollmentResponseDTO = EnrollmentResponseDTO.builder()
            .enrollmentId("1234")
            .enrollmentYear(2023)
            .semester(Semester.FALL)
            .studentId(studentResponseDTO.getStudentId())
            .courseId(courseResponseDTO.getCourseId())
            .build();
    @Test
    void getByValidEnrollmentId() {
        when(enrollmentService.getEnrollmentByEnrollmentId(anyString()))
                .thenReturn(Mono.just(enrollmentResponseDTO));
        webTestClient
                .get()
                .uri("/enrollments/{enrollmentId}", enrollmentResponseDTO.getEnrollmentId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(EnrollmentResponseDTO.class)
                .value((enrollmentResponseDTO) -> {
                    assertNotNull(enrollmentResponseDTO);
                    assertNotNull(enrollmentResponseDTO.getEnrollmentId());
                    assertEquals(enrollmentRequestDTO.getEnrollmentYear(), enrollmentResponseDTO.getEnrollmentYear());
                    assertEquals(enrollmentRequestDTO.getSemester(), enrollmentResponseDTO.getSemester());
                    assertEquals(enrollmentRequestDTO.getStudentId(), enrollmentResponseDTO.getStudentId());
                    assertEquals(enrollmentRequestDTO.getCourseId(), enrollmentResponseDTO.getCourseId());
                });
    }

    @Test
    void updateEnrollmentByEnrollmentId(){
        when(enrollmentService.updateEnrollment(anyString(), any()))
                .thenReturn(Mono.just(enrollmentResponseDTO));
        webTestClient
                .put()
                .uri("/enrollments/{enrollmentId}", enrollmentResponseDTO.getEnrollmentId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(enrollmentRequestDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(EnrollmentResponseDTO.class)
                .value((enrollmentResponseDTO) -> {
                    assertNotNull(enrollmentResponseDTO);
                    assertNotNull(enrollmentResponseDTO.getEnrollmentId());
                    assertEquals(enrollmentRequestDTO.getEnrollmentYear(), enrollmentResponseDTO.getEnrollmentYear());
                    assertEquals(enrollmentRequestDTO.getSemester(), enrollmentResponseDTO.getSemester());
                    assertEquals(enrollmentRequestDTO.getStudentId(), enrollmentResponseDTO.getStudentId());
                    assertEquals(enrollmentRequestDTO.getCourseId(), enrollmentResponseDTO.getCourseId());
                });
    }

    @Test
    void deleteByEnrollmentId() {
        when(enrollmentService.deleteEnrollment(anyString()))
                .thenReturn(Mono.empty());

        webTestClient
                .delete()
                .uri("/enrollments/{enrollmentId}", enrollmentResponseDTO.getEnrollmentId())
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNoContent();
    }


}