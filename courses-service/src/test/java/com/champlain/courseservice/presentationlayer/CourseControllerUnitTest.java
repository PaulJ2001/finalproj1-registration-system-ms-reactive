package com.champlain.courseservice.presentationlayer;

import com.champlain.courseservice.buisnesslayer.CourseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = CourseController.class)
class CourseControllerUnitTest {

    private CourseResponseDTO dto = buildCourseDTO();

    private final String COURSE_ID_OKAY_UUID = dto.getCourseId();

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    CourseService courseService;

    @Test
    void getCourseByCourseId() {
        //arrange
        when(courseService.getCourseByCourseId(anyString()))
                .thenReturn(Mono.just(dto));

        //act and assert

        webTestClient
                .get()
                .uri("/courses/" + COURSE_ID_OKAY_UUID)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.courseId").isEqualTo(dto.getCourseId())
                .jsonPath("$.courseNumber").isEqualTo(dto.getCourseNumber())
                .jsonPath("$.courseName").isEqualTo(dto.getCourseName())
                .jsonPath("$.numHours").isEqualTo(dto.getNumHours())
                .jsonPath("$.numCredits").isEqualTo(dto.getNumCredits())
                .jsonPath("$.department").isEqualTo(dto.getDepartment());

        Mockito.verify(courseService, times(1))
                .getCourseByCourseId(COURSE_ID_OKAY_UUID);
    }

    private CourseResponseDTO buildCourseDTO() {
        return CourseResponseDTO.builder()
                .courseId("123")
                .courseNumber("420B02")
                .courseName(".NET")
                .numHours(6)
                .numCredits(6.0)
                .department("Comp Sci")
                .build();


    }

}