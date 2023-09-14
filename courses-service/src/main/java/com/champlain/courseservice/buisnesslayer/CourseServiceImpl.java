package com.champlain.courseservice.buisnesslayer;

import com.champlain.courseservice.Utils.EntityDTOUtils;
import com.champlain.courseservice.Utils.exceptions.NotFoundException;
import com.champlain.courseservice.dataaccesslayer.CourseRepository;
import com.champlain.courseservice.presentationlayer.CourseRequestDTO;
import com.champlain.courseservice.presentationlayer.CourseResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;   //make Final


    @Override
    public Flux<CourseResponseDTO> getAllCourses() {
        return courseRepository.findAll()
                .map(EntityDTOUtils::toCourseResponseDTO);

    }

    @Override
    public Mono<CourseResponseDTO> getCourseByCourseId(String courseId) {
        return courseRepository.findCourseByCourseId(courseId)
                .map(EntityDTOUtils::toCourseResponseDTO)
                .switchIfEmpty(Mono.error(new NotFoundException("CourseId: " +  courseId + " not found")));
    }

    @Override
    public Mono<CourseResponseDTO> addCourse(@RequestBody Mono<CourseRequestDTO> courseRequestDTO) {
        return courseRequestDTO
                .map(EntityDTOUtils::toCourseEntity)
                .doOnNext(e -> e.setCourseId(EntityDTOUtils.generateUUIDString()))
                .flatMap(courseRepository::insert)
                .map(EntityDTOUtils::toCourseResponseDTO);
    }

    @Override
    public Mono<Void> removeCourse(String courseId) {
        return courseRepository.findCourseByCourseId(courseId)
                .flatMap(courseRepository::delete);
    }

    @Override
    public Mono<CourseResponseDTO> updateCourse(String courseId, Mono<CourseRequestDTO> courseRequestDTO) {
        return courseRepository.findCourseByCourseId(courseId)
                .flatMap(c -> courseRequestDTO
                        .map(EntityDTOUtils::toCourseEntity)
                                .doOnNext(e -> e.setCourseId(c.getCourseId()))
                                .doOnNext(e -> e.setId(c.getId()))
                )
                    .flatMap(courseRepository::save)
                    .map(EntityDTOUtils::toCourseResponseDTO);

    }




}
