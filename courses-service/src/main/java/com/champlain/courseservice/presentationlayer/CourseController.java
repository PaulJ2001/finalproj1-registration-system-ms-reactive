package com.champlain.courseservice.presentationlayer;

import com.champlain.courseservice.buisnesslayer.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;

@RestController
@RequestMapping("courses")
@Slf4j
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping(value = "", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<CourseResponseDTO> getAllCourses(){
        return courseService.getAllCourses();
    }

    @PostMapping()
    public Mono<ResponseEntity<CourseResponseDTO>> addCourse(@RequestBody Mono<CourseRequestDTO> courseRequestDTO) throws URISyntaxException{
        return courseService.addCourse(courseRequestDTO)
                .map(s -> ResponseEntity.status(HttpStatus.CREATED).body(s))
                .defaultIfEmpty(ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "{courseId}")
    public Mono<ResponseEntity<CourseResponseDTO>> getCourseByCourseId(@PathVariable String courseId){
        return courseService.getCourseByCourseId(courseId)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PutMapping(value = "{courseId}")
    public Mono<ResponseEntity<CourseResponseDTO>> updateCourse(@PathVariable String courseId, @RequestBody Mono<CourseRequestDTO> courseRequestDTO ){
        return courseService.updateCourse(courseId, courseRequestDTO)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "{courseId}")
    public Mono<ResponseEntity<Void>> deleteCourse(@PathVariable String courseId){
        return courseService.removeCourse(courseId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }


}
