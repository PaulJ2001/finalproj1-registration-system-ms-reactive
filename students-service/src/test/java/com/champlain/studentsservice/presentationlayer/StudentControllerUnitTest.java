package com.champlain.studentsservice.presentationlayer;

import com.champlain.studentsservice.buisnessLayer.StudentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@WebFluxTest(controllers = StudentController.class)
class StudentControllerUnitTest {

    private StudentResponseDTO dto = buildStudentDTO();


    private final String STUDENT_ID_OKAY_UUID = dto.getStudentId();


    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    StudentService studentService;

    @Test
    void getStudentByStudentId() {
        //arrange
        when(studentService.getStudentByStudentId(anyString()))
                .thenReturn(Mono.just(dto));

        //act and assert

        webTestClient
                .get()
                .uri("/students/" + STUDENT_ID_OKAY_UUID)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.studentId").isEqualTo(dto.getStudentId())
                .jsonPath("$.firstName").isEqualTo(dto.getFirstName())
                .jsonPath("$.lastName").isEqualTo(dto.getLastName())
                .jsonPath("$.program").isEqualTo(dto.getProgram());


        verify(studentService, times(1))
                .getStudentByStudentId(STUDENT_ID_OKAY_UUID);
    }

    @Test
    void getStudentByStudentId_NotFound() {
        // Arrange
        when(studentService.getStudentByStudentId(anyString()))
                .thenReturn(Mono.empty());

        // Act and Assert
        webTestClient
                .get()
                .uri("/students/nonExistentStudentId")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();

        // Verify that the service method was called once
        verify(studentService, times(1))
                .getStudentByStudentId("nonExistentStudentId");
    }


    private StudentResponseDTO buildStudentDTO() {
        return StudentResponseDTO.builder()
                .studentId("12344-22")
                .firstName("John")
                .lastName("Doe")
                .program("Comp Sci")
                .build();
    }

    private StudentRequestDTO buildStudentRequestDTO() {
        return StudentRequestDTO.builder()
                .firstName("Jane")
                .lastName("Smith")
                .program("Math")
                .build();
    }
}