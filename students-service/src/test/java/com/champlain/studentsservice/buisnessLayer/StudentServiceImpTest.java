package com.champlain.studentsservice.buisnessLayer;


import com.champlain.studentsservice.dataaccesslayer.Student;
import com.champlain.studentsservice.dataaccesslayer.StudentRepository;
import com.champlain.studentsservice.presentationlayer.StudentResponseDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@SpringBootTest
class StudentServiceImplTest {

    @Autowired
    private StudentService studentService;

    @MockBean
    private StudentRepository studentRepository;

    @Test
    void getStudentByStudentId(){
        //arrange
        Student student = buildStudent();
        String STUDENT_ID = student.getStudentId();

        when(studentRepository.findStudentByStudentId(anyString())).thenReturn(Mono.just(student));

        //act
        Mono<StudentResponseDTO> studentResponseDTOMono = studentService.getStudentByStudentId(STUDENT_ID);

        //assert
        StepVerifier
                .create(studentResponseDTOMono)
                .consumeNextWith(foundStudent -> {
                    assertEquals(student.getStudentId(), foundStudent.getStudentId());
                    assertEquals(student.getFirstName(), foundStudent.getFirstName());
                    assertEquals(student.getLastName(), foundStudent.getLastName());
                    assertEquals(student.getProgram(), foundStudent.getProgram());

                })
                .verifyComplete();
    }

    private Student buildStudent(){
        return Student.builder()
                .studentId("1234-22")
                .firstName("Mary")
                .lastName("Sue")
                .program("Social Science")
                .build();
    }
}

