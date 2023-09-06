package com.champlain.sectionsservice.presentationlayer;


import com.champlain.sectionsservice.buisnesslayer.EnrollmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;
import java.util.Map;

@RestController
@RequestMapping("enrollments")
@Slf4j
@RequiredArgsConstructor

public class EnrollmentController {

    private final EnrollmentService enrollmentService;



    @GetMapping()
    public Flux<EnrollmentResponseDTO> GetAllEnrollments(@RequestParam(required = false)Map<String, String> queryParams){
        return enrollmentService.getAllEnrollments(queryParams);
    }


    @PostMapping()
    public Mono<ResponseEntity<EnrollmentResponseDTO>> addEnrollment(@RequestBody Mono<EnrollmentRequestDTO> enrollmentRequestDTO) throws URISyntaxException {
        return enrollmentService.addEnrollment(enrollmentRequestDTO)
                .doOnNext(i -> System.out.println(  "I'm in addEnrollment in Service - before map"))
                .map(e -> ResponseEntity.status(HttpStatus.CREATED).body(e))
                .doOnNext(i -> System.out.println("I'm in taddEnrollment in Controller" + i.toString()))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "{enrollmentId}")
    public Mono<ResponseEntity<EnrollmentResponseDTO>> getEnrollmentByEnrollmentId(@PathVariable String enrollmentId){
        return enrollmentService.getEnrollmentByEnrollmentId(enrollmentId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @DeleteMapping(value = "{enrollmentId}")
    public Mono<ResponseEntity<Void>> deleteEnrollment(@PathVariable String enrollmentId){
        return enrollmentService.deleteEnrollment(enrollmentId)
                .then(Mono.just(ResponseEntity.noContent().build()));

    }


    @PutMapping(value = "{enrollmentId}")
    public Mono<ResponseEntity<EnrollmentResponseDTO>> updateEnrollment(@PathVariable String enrollmentId, @RequestBody Mono<EnrollmentRequestDTO> enrollmentRequestDTO){
        return enrollmentService.updateEnrollment(enrollmentId, enrollmentRequestDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }






}
