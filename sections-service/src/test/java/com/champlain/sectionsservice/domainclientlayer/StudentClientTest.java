package com.champlain.sectionsservice.domainclientlayer;


import com.champlain.sectionsservice.buisnesslayer.StudentResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

@WebFluxTest(StudentClient.class)
class StudentClientTest {

    @MockBean
    private ConnectionFactoryInitializer connectionFactoryInitializer;

    @MockBean
    private StudentClient studentClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }
    @BeforeEach
    void initialize() {
        studentClient = new StudentClient("localhost", String.valueOf(mockBackEnd.getPort()));
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void testGetStudentByValidStudentId() throws Exception {
        StudentResponseDTO studentResponseDTO = new StudentResponseDTO("student123", "Clem", " Cadew", "History");

        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(studentResponseDTO))
                .addHeader("Content-Type", "application/json"));

        Mono<StudentResponseDTO> studentResponseDTOMono = studentClient.getStudentByStudentId("student123");
        StepVerifier.create(studentResponseDTOMono)
                .expectNextMatches(studentResponseDTO1 -> studentResponseDTO1.getStudentId().equals("student123"))
                .verifyComplete();
    }




}