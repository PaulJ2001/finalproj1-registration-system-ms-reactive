package com.champlain.courseservice.buisnesslayer;


import com.champlain.courseservice.presentationlayer.CourseRequestDTO;
import com.champlain.courseservice.presentationlayer.CourseResponseDTO;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface CourseService {

    Flux<CourseResponseDTO> getAllCourses();

    Mono<CourseResponseDTO> getCourseByCourseId(String courseId);

    Mono<CourseResponseDTO> addCourse(Mono<CourseRequestDTO> courseRequestDTO);

    Mono<Void> removeCourse(String courseId);

    Mono<CourseResponseDTO> updateCourse(String courseId, Mono<CourseRequestDTO> courseRequestDTO);




}
