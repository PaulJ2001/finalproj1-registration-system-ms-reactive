package com.champlain.studentsservice.presentationlayer;


import com.champlain.studentsservice.dataaccesslayer.Student;
import com.champlain.studentsservice.dataaccesslayer.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {"spring.data.mongodb.port: 0"})
@AutoConfigureWebTestClient
class StudentControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private StudentRepository studentRepository;

    private String validStudentId;

    private final Long dbSize = 4L;

    @BeforeEach
    public void dbSetup() {

        Student student1 = buildStudent("jones", "studentId_1");
        Student student2 = buildStudent("bueller", "studentId_2");
        Student student3 = buildStudent("kong", "studentId_3");
        Student student4 = buildStudent("tremblay", "studentId_4");

        Publisher<Student> setup = studentRepository.deleteAll()
                .thenMany(studentRepository.save(student1))
                .thenMany(studentRepository.save(student2))
                .thenMany(studentRepository.save(student3))
                .thenMany(studentRepository.save(student4));

        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();

    }

    @Test
    void getAllStudents() {

        webTestClient
                .get()
                .uri("/students")
                .accept(MediaType.valueOf(MediaType.TEXT_EVENT_STREAM_VALUE))
                .acceptCharset(StandardCharsets.UTF_8)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().valueEquals("Content-Type", "text/event-stream;charset=UTF-8")
                .expectBodyList(StudentResponseDTO.class)
                .value((list) -> {
                    assertNotNull(list);
                    assertEquals(dbSize, list.size());
                });
    }

    @Test
    void getStudentByStudentIdString_withValidStudentId() {

        Mono<Student> studentMono = Mono.from(studentRepository.findAll()
                .doOnNext(student -> {
                    validStudentId = student.getStudentId();
                    System.out.println(validStudentId);
                }));

        StepVerifier.create(studentMono)
                .expectNextCount(1)
                .verifyComplete();

        webTestClient
                .get()
                .uri("/students/{studentId}", validStudentId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.studentId").isEqualTo(validStudentId);
    }

    @Test
    void getStudentByStudentIdString_withInValidStudentId_throwsNotFoundException() {

        String invalidStudentId = "123";

        webTestClient
                .get()
                .uri("/students/{studentId}", invalidStudentId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.message").isEqualTo("Student Id: " + invalidStudentId + " not found");

    }

    @Test
    void addStudent() {

        StudentRequestDTO newStudentDTO = StudentRequestDTO.builder()
                .firstName("Donna")
                .lastName("AppleBee")
                .program("Tourism")
                .build();

        webTestClient
                .post()
                .uri("/students")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newStudentDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody(StudentResponseDTO.class)
                .value(studentResponseDTO -> {
                    assertNotNull(studentResponseDTO);
                    assertNotNull(studentResponseDTO.getStudentId());
                    assertThat(studentResponseDTO.getLastName()).isEqualTo(newStudentDTO.getLastName());
                });
    }

    @Test
    void updateStudentShouldSucceed() {

        Mono<Student> studentMono = Mono.from(studentRepository.findAll()
                .doOnNext(student -> {
                    validStudentId = student.getStudentId();
                    System.out.println(validStudentId);
                }));

        StepVerifier.create(studentMono)
                .expectNextCount(1)
                .verifyComplete();

        StudentRequestDTO newStudentDTO = StudentRequestDTO.builder()
                .firstName("Donna")
                .lastName("AppleBee")
                .program("Tourism")
                .build();

        webTestClient
                .put()
                .uri("/students/{studentId}", validStudentId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newStudentDTO)
                .exchange()
                .expectStatus().isOk()
                .expectBody(StudentResponseDTO.class)
                .value(studentResponseDTO -> {
                    assertNotNull(studentResponseDTO);
                    assertNotNull(studentResponseDTO.getStudentId());
                    assertThat(studentResponseDTO.getLastName()).isEqualTo(newStudentDTO.getLastName());
                });


    }

    @Test
    void deleteStudentShouldSucceed() {
        Mono<Student> studentMono = Mono.from(studentRepository.findAll()
                .doOnNext(student -> {
                    validStudentId = student.getStudentId();
                    System.out.println(validStudentId);
                }));

        StepVerifier.create(studentMono)
                .expectNextCount(1)
                .verifyComplete();

        webTestClient
                .delete()
                .uri("/students/{studentId}", validStudentId)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound();


    }

    @Test
    void testFirstNameTooLong() {

        StudentRequestDTO newStudentDTO = StudentRequestDTO.builder()
                .firstName("Donnajskoskps[wpl[l[elekekediindbfbfjrrujnfzzzzzzzzzzzzzzzzzzzzzzzzzzz")
                .lastName("AppleBee")
                .program("Tourism")
                .build();

        webTestClient
                .post()
                .uri("/students")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newStudentDTO)
                .exchange()
                .expectStatus().isEqualTo(org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message").isEqualTo("first name exceeds character limit");

    }

    @Test
    void testLastNameTooLong() {

        StudentRequestDTO newStudentDTO = StudentRequestDTO.builder()
                .firstName("Donna")
                .lastName("AppleBeejskoskps[wpl[l[elekekediindbfbfjrrujnf")
                .program("Tourism")
                .build();

        webTestClient
                .post()
                .uri("/students")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newStudentDTO)
                .exchange()
                .expectStatus().isEqualTo(org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY)
                //.expectStatus().isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR)
                .expectBody()
                .jsonPath("$.message").isEqualTo("last name exceeds character limit");

    }

    @Test
    void testProgramTooLong() {

        StudentRequestDTO newStudentDTO = StudentRequestDTO.builder()
                .firstName("Donna")
                .lastName("AppleBee")
                .program("Tourismjskoskps[wpl[l[elekekediindbfbfjrrujnf")
                .build();

        webTestClient
                .post()
                .uri("/students")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(newStudentDTO)
                .exchange()
                .expectStatus().isEqualTo(org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY)
                .expectBody()
                .jsonPath("$.message").isEqualTo("program name exceeds character limit");

    }


    private Student buildStudent(String lastName, String studentId) {
        return Student.builder()
                .studentId(studentId)
                .firstName("Jane")
                .lastName(lastName)
                .program("Computer Science Technology")
                .build();
    }

}