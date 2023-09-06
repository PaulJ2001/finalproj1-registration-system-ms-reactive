package com.champlain.sectionsservice.buisnesslayer;

import com.champlain.sectionsservice.presentationlayer.EnrollmentResponseDTO;
import com.champlain.sectionsservice.presentationlayer.EnrollmentRequestDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface EnrollmentService {

    Mono<EnrollmentResponseDTO> addEnrollment(Mono<EnrollmentRequestDTO> enrollmentRequestDTO);

    Flux<EnrollmentResponseDTO> getAllEnrollments(Map<String, String> queryParams);

    Mono<EnrollmentResponseDTO> getEnrollmentByEnrollmentId(String enrollmentId);

    Mono<Void> deleteEnrollment (String enrollmentId);

    Mono<EnrollmentResponseDTO> updateEnrollment(String enrollmentId, Mono<EnrollmentRequestDTO> enrollmentRequestDTO);




}
