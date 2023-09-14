package com.champlain.studentsservice.dataaccesslayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

//@SpringBootTest(webEnvironment = RANDOM_PORT, properties = {"spring.data.mongodb.port: 0"})
@DataMongoTest
class StudentRepositoryTest {

    @Autowired
    StudentRepository studentRepository;

    Student student1;
    Student student2;

    @BeforeEach
    public void setupDB(){
        student1 = buildStudent("Smith", "studentId_1");
        Publisher<Student> setup1 =studentRepository.deleteAll()
                .thenMany(studentRepository.save(student1));

        StepVerifier
                .create(setup1)
                .expectNextCount(1)
                .verifyComplete();

        student2 = buildStudent("Tang", "studentId_2");
        Publisher<Student> setup2 = studentRepository
                .save(student2);

        StepVerifier
                .create(setup2)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void shouldSaveSingleStudent(){
        //arrange
        Student newStudent = buildStudent("Bobby", "studentId_3");
        Publisher<Student> setup = studentRepository.save(newStudent);

        //act and assert
        StepVerifier
                .create(setup)
                .expectNextCount(1)
                .verifyComplete();
    }


    @Test public void findStudentByStudentID_shouldFindOne(){
        StepVerifier
                .create(studentRepository.findStudentByStudentId(student2.getStudentId()))
                .assertNext(student -> {
                    assertThat(student.getStudentId()).isEqualTo(student2.getStudentId());
                    assertThat(student.getLastName()).isEqualTo(student2.getLastName());
                });
    }

    @Test public void findStudentByLastName(){
        StepVerifier
                .create(studentRepository.findStudentByStudentId(student2.getStudentId()))
                .assertNext(student -> {
                    assertThat(student.getLastName()).isEqualTo(student2.getLastName());
                });
    }

    @Test
    public void shouldGetAllStudents(){
        //act and assert
        StepVerifier
                .create(studentRepository.findAll())
                .expectNextCount(2)
                .verifyComplete();
    }


    @Test
    public void shouldDeleteAllStudents() {
        StepVerifier
                .create(studentRepository.deleteAll())
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    public void shouldUpdateStudent() {
        // Arrange
        student1.setFirstName("John");

        // Act and Assert
        StepVerifier
                .create(studentRepository.save(student1))
                .expectNextCount(1)
                .verifyComplete();

        // Verify that the updated student data is reflected in the repository
        StepVerifier
                .create(studentRepository.findStudentByStudentId(student1.getStudentId()))
                .assertNext(updatedStudent -> {
                    assertThat(updatedStudent.getFirstName()).isEqualTo("John");
                })
                .verifyComplete();
    }

//    @Test
//    public void shouldCountStudents() {
//        // Act and Assert
//        StepVerifier
//                .create(studentRepository.count())
//                .expectNext(2L) // Expecting 2 students in the database
//                .verifyComplete();
//    }


    private Student buildStudent(String lastName, String studentId){
        return Student.builder()
                .studentId(studentId)
                .firstName("Marry")
                .lastName(lastName)
                .program("CompSci")
                .build();
    }





}


