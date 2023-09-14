package com.champlain.sectionsservice.buisnesslayer;

import com.champlain.sectionsservice.Utils.EntityDTOUtils;
import com.champlain.sectionsservice.Utils.exceptions.InvalidInputException;
import com.champlain.sectionsservice.Utils.exceptions.NotFoundException;
import com.champlain.sectionsservice.dataaccesslayer.EnrollmentRepository;
import com.champlain.sectionsservice.domainclientlayer.CourseClient;
import com.champlain.sectionsservice.domainclientlayer.StudentClient;
import com.champlain.sectionsservice.presentationlayer.EnrollmentRequestDTO;
import com.champlain.sectionsservice.presentationlayer.EnrollmentResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService{

    private final EnrollmentRepository enrollmentRepository;
    private final StudentClient studentClient;
    private  final CourseClient courseClient;

    @Override
    public Mono<EnrollmentResponseDTO> addEnrollment(Mono<EnrollmentRequestDTO> enrollmentRequestDTO) {
        return enrollmentRequestDTO
                .map(RequestContextAdd:: new)
                .flatMap(this::studentRequestResponse)
                .flatMap(this::courseRequestResponse)
                .map(EntityDTOUtils::toEnrollmentEntity)
                .map(enrollmentRepository::save)
                .flatMap(entity -> entity)
                .doOnNext(i -> System.out.println("the entity is: " + i.getStudentId()))
                .map(EntityDTOUtils::toEnrollmentResponseDTO);

    }

    @Override
    public Flux<EnrollmentResponseDTO> getAllEnrollments(Map<String, String> queryParams) {

        String course = queryParams.get("courseId");
        String student = queryParams.get("studentId");
        String enrollYear = (queryParams.get("enrollYear"));
        Integer enrollmentYear = null;

        try {
            if (enrollYear != null) {
                enrollmentYear = Integer.parseInt(enrollYear);
            }
        }catch(Exception ex){
            return Flux.error(new InvalidInputException("Invalid Enrollment year: " + enrollmentYear + " Please Confirm Valid Interger Enrollment year"));
        }

        if (student != null && course != null && enrollmentYear != null){
            return enrollmentRepository.findEnrollmentByStudentIdAndCourseIdAndEnrollmentYear(student, course, enrollmentYear)
                    .map(EntityDTOUtils::toEnrollmentResponseDTO);
        }

        if(course != null && enrollmentYear != null){
            return enrollmentRepository.findEnrollmentByCourseIdAndEnrollmentYear(course, enrollmentYear)
                    .map(EntityDTOUtils::toEnrollmentResponseDTO);
        }

        if(student != null && enrollmentYear != null){
            return enrollmentRepository.findEnrollmentByStudentIdAndEnrollmentYear(student, enrollmentYear)
                    .map(EntityDTOUtils::toEnrollmentResponseDTO);
        }

        if(student != null && course != null){
            return enrollmentRepository.findEnrollmentByStudentIdAndCourseId(student, course)
                    .map(EntityDTOUtils::toEnrollmentResponseDTO);
        }

        if(student != null){
            return enrollmentRepository.findEnrollmentByStudentId(student)
                    .map(EntityDTOUtils::toEnrollmentResponseDTO);
        }

        if(course != null){
            return enrollmentRepository.findEnrollmentByCourseId(course)
                    .map(EntityDTOUtils::toEnrollmentResponseDTO);
        }

        if(enrollmentYear != null){
            return  enrollmentRepository.findEnrollmentByEnrollmentYear(enrollmentYear)
                    .map(EntityDTOUtils::toEnrollmentResponseDTO);
        }







        return enrollmentRepository.findAll()
                .map(EntityDTOUtils::toEnrollmentResponseDTO);
    }

    @Override
    public Mono<EnrollmentResponseDTO> getEnrollmentByEnrollmentId(String enrollmentId) {
        return enrollmentRepository.getEnrollmentByEnrollmentId(enrollmentId)
                .switchIfEmpty(Mono.error(new NotFoundException("Enrollment Id: " + enrollmentId  + "not found")))
                .map(EntityDTOUtils::toEnrollmentResponseDTO);
    }

    @Override
    public Mono<Void> deleteEnrollment(String enrollmentId) {
        return enrollmentRepository.getEnrollmentByEnrollmentId(enrollmentId)
                .switchIfEmpty(Mono.error(new NotFoundException("Enrollment Id: " + enrollmentId + " not found")))
                .flatMap(enrollmentRepository::delete);
    }

    @Override
    public Mono<EnrollmentResponseDTO> updateEnrollment(String enrollmentId, Mono<EnrollmentRequestDTO> enrollmentRequestDTO) {
        return enrollmentRepository.getEnrollmentByEnrollmentId(enrollmentId)
                .switchIfEmpty(Mono.error(new NotFoundException("Enrollment Id: " + enrollmentId + " not found")))

                .flatMap(
                        enrollDTO -> enrollmentRequestDTO
                        .map(RequestContextAdd::new)
                        .flatMap(this::studentRequestResponse)
                        .flatMap(this::courseRequestResponse)
                        .map(EntityDTOUtils::toEnrollmentEntity)
                        .doOnNext(e -> e.setId(enrollDTO.getId()))
                        .doOnNext(e -> e.setEnrollmentId(enrollmentId))
                        .flatMap(enrollmentRepository::save)

                        .map(EntityDTOUtils::toEnrollmentResponseDTO));

    }

    private Mono<RequestContextAdd> courseRequestResponse(RequestContextAdd rc) {
        return this.courseClient.getCourseByCourseId(rc.getEnrollmentRequestDTO().getCourseId())
                .doOnNext(rc::setCourseResponseDTO)
                .thenReturn(rc);
    }

    private Mono<RequestContextAdd> studentRequestResponse(RequestContextAdd rc){
        return this.studentClient.getStudentByStudentId(rc.getEnrollmentRequestDTO().getStudentId())
                .doOnNext(rc::setStudentResponseDTO)
                .thenReturn(rc);
    }



}

