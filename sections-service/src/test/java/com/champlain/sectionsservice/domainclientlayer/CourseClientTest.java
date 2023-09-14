package com.champlain.sectionsservice.domainclientlayer;


import com.champlain.sectionsservice.buisnesslayer.CourseResponseDTO;
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

@WebFluxTest(CourseClient.class)
class CourseClientTest {

    @MockBean
    private ConnectionFactoryInitializer connectionFactoryInitializer;

    @MockBean
    private CourseClient courseClient;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static MockWebServer mockBackEnd;

    @BeforeAll
    static void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
    }
    @BeforeEach
    void initialize() {
        courseClient = new CourseClient("localhost", String.valueOf(mockBackEnd.getPort()));
    }

    @AfterAll
    static void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    void testGetCourseByValidCourseId() throws Exception {
        CourseResponseDTO courseResponseDTO = new CourseResponseDTO("course123", "420b02", "Java 2", 6, 5.0, "Comp Sci");

        mockBackEnd.enqueue(new MockResponse()
                .setBody(objectMapper.writeValueAsString(courseResponseDTO))
                .addHeader("Content-Type", "application/json"));

        Mono<CourseResponseDTO> courseResponseDTOMono = courseClient.getCourseByCourseId("course123");
        StepVerifier.create(courseResponseDTOMono)
                .expectNextMatches(courseResponseDTO1 -> courseResponseDTO1.getCourseId().equals("course123"))
                .verifyComplete();
    }

}