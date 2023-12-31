package com.champlain.studentsservice.buisnessLayer;

import com.champlain.studentsservice.presentationlayer.StudentRequestDTO;
import com.champlain.studentsservice.presentationlayer.StudentResponseDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface StudentService {
    Flux<StudentResponseDTO> getAllStudents();
    Mono<StudentResponseDTO> addStudent(Mono<StudentRequestDTO> studentRequestDTO);

    Mono<StudentResponseDTO> getStudentByStudentId(String studentId);

    Mono<Void> deleteStudent(String studentId);

    Mono<StudentResponseDTO> updateStudent(String studentId, Mono<StudentRequestDTO> studentRequestDTO)
            ;

}
