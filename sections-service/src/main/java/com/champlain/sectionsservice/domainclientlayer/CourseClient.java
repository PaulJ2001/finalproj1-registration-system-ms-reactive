package com.champlain.sectionsservice.domainclientlayer;



import com.champlain.sectionsservice.buisnesslayer.CourseResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class CourseClient {



    private final WebClient webClient;
    private final String courseClientServiceBaseURL;

    public CourseClient (@Value("${app.courses-service.host}") String courseServiceHost,
                         @Value("${app.courses-service.port}") String courseServicePort) {


        courseClientServiceBaseURL = "http://" + courseServiceHost + ":" + courseServicePort + "/courses";

        this.webClient = WebClient.builder()
                .baseUrl(courseClientServiceBaseURL)
                .build();

    }

    public Mono<CourseResponseDTO> getCourseByCourseId(final String courseId){
        return this.webClient
                .get()
                .uri("/{courseId}", courseId)
                .retrieve()
                .bodyToMono(CourseResponseDTO.class);
    }

        // public static String generateUUIDString(){ return UUID.randomUUID().toString();}



}
