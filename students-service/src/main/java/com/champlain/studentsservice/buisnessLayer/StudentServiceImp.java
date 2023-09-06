package com.champlain.studentsservice.buisnessLayer;

import com.champlain.studentsservice.Utils.EntityDTOUtils;
import com.champlain.studentsservice.dataaccesslayer.StudentRepository;
import com.champlain.studentsservice.presentationlayer.StudentRequestDTO;
import com.champlain.studentsservice.presentationlayer.StudentResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class StudentServiceImp  implements StudentService{

    private final StudentRepository studentRepository;

    @Override
    public Flux<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll()
                .map(EntityDTOUtils::toStudentResponseDTO);
    }

    @Override
    public Mono<StudentResponseDTO> addStudent(@RequestBody Mono<StudentRequestDTO> studentRequestDTO) {
        return studentRequestDTO
                .map(EntityDTOUtils::toStudentEntity)
                .doOnNext(e -> e.setStudentId(EntityDTOUtils.generateUUIDString()))
                .flatMap(studentRepository::insert)
                .map(EntityDTOUtils::toStudentResponseDTO);

    }

    @Override
    public Mono<StudentResponseDTO> getStudentByStudentId(String studentId) {
        return studentRepository.findStudentByStudentId(studentId)
                .map(EntityDTOUtils::toStudentResponseDTO);

    }

    @Override
    public Mono<Void> deleteStudent(String studentId) {
        return studentRepository.findStudentByStudentId(studentId)
                .flatMap(studentRepository::delete);



    }


    @Override
    public Mono<StudentResponseDTO> updateStudent(String studentId, Mono<StudentRequestDTO> studentRequestDTO) {
        return studentRepository.findStudentByStudentId(studentId)
                .flatMap(student -> studentRequestDTO
                                .map(EntityDTOUtils::toStudentEntity)
                                .doOnNext(e ->
                                    e.setStudentId(student.getStudentId()))
                                .doOnNext(e -> e.setId(student.getId()))
                .flatMap(studentRepository::save)
                .map(EntityDTOUtils::toStudentResponseDTO));

    }







}



