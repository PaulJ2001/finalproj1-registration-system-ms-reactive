package com.champlain.sectionsservice.domainclientlayer;

import com.champlain.sectionsservice.buisnesslayer.StudentResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class StudentClient {


    private final WebClient webClient;
    private final String studentClientServiceBaseURL;

    public StudentClient(@Value("${app.students-service.host}") String studentServiceHost,
                         @Value("${app.students-service.port}") String studentServicePort) {


        studentClientServiceBaseURL = "http://" + studentServiceHost + ":" + studentServicePort + "/students";

        this.webClient = WebClient.builder()
                .baseUrl(studentClientServiceBaseURL)
                .build();

    }

    public Mono<StudentResponseDTO> getStudentByStudentId(final String studentId) {
        return this.webClient
                .get()
                .uri("/{studentId}", studentId)
                .retrieve()
                .bodyToMono(StudentResponseDTO.class);
    }
}
