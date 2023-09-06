package com.champlain.sectionsservice.Utils;

import com.champlain.sectionsservice.buisnesslayer.RequestContextAdd;
import com.champlain.sectionsservice.dataaccesslayer.Enrollment;
import com.champlain.sectionsservice.presentationlayer.EnrollmentResponseDTO;
import org.springframework.beans.BeanUtils;
import java.util.UUID;

public class EntityDTOUtils {



    public static EnrollmentResponseDTO toEnrollmentResponseDTO(Enrollment enrollment){
        EnrollmentResponseDTO enrollmentResponseDTO = new EnrollmentResponseDTO();
        BeanUtils.copyProperties(enrollment, enrollmentResponseDTO);
        return enrollmentResponseDTO;
    }



    public static Enrollment toEnrollmentEntity(RequestContextAdd rc) {
        return Enrollment.builder()
                .enrollmentId(generateUUIDString())
                .enrollmentYear(rc.getEnrollmentRequestDTO().getEnrollmentYear())
                .semester(rc.getEnrollmentRequestDTO().getSemester())
                .studentId(rc.getStudentResponseDTO().getStudentId())
                .studentFirstName(rc.getStudentResponseDTO().getFirstName())
                .studentLastName(rc.getStudentResponseDTO().getLastName())
                .courseId(rc.getCourseResponseDTO().getCourseId())
                .courseName(rc.getCourseResponseDTO().getCourseName())
                .courseNumber(rc.getCourseResponseDTO().getCourseNumber())
                .build();
    }

   public static String generateUUIDString() {
        return UUID.randomUUID().toString();
    }
}
