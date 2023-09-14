package com.champlain.studentsservice.Utils;

import com.champlain.studentsservice.Utils.exceptions.InvalidInputException;
import com.champlain.studentsservice.dataaccesslayer.Student;
import com.champlain.studentsservice.presentationlayer.StudentRequestDTO;
import com.champlain.studentsservice.presentationlayer.StudentResponseDTO;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

public class EntityDTOUtils {

    public static StudentResponseDTO toStudentResponseDTO(Student student){
        StudentResponseDTO studentResponseDTO = new StudentResponseDTO();
        BeanUtils.copyProperties(student , studentResponseDTO);
        return studentResponseDTO;

    }

    public static Student toStudentEntity(StudentRequestDTO studentRequestDTO) {
        Student student = new Student();
        if (studentRequestDTO.getFirstName().length() > 20){
            throw new InvalidInputException("first name exceeds character limit");
        }
        if (studentRequestDTO.getLastName().length() > 20){
            throw new InvalidInputException("last name exceeds character limit");
        }
        if (studentRequestDTO.getProgram().length() > 20){
            throw new InvalidInputException("program name exceeds character limit");
        }
        BeanUtils.copyProperties(studentRequestDTO , student);
        return student;
    }

    public static String generateUUIDString(){
        return UUID.randomUUID().toString();
    }
}

