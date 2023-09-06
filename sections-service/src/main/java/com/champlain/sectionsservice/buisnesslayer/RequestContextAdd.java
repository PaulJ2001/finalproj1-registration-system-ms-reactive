package com.champlain.sectionsservice.buisnesslayer;

import com.champlain.sectionsservice.buisnesslayer.StudentResponseDTO;
import com.champlain.sectionsservice.buisnesslayer.CourseResponseDTO;
import com.champlain.sectionsservice.dataaccesslayer.Enrollment;
import com.champlain.sectionsservice.presentationlayer.EnrollmentRequestDTO;
import com.champlain.sectionsservice.presentationlayer.EnrollmentResponseDTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestContextAdd {

    private EnrollmentRequestDTO enrollmentRequestDTO;
    private Enrollment enrollment;
    private StudentResponseDTO studentResponseDTO;
    private CourseResponseDTO courseResponseDTO;
   // private EnrollmentResponseDTO enrollmentResponseDTO;

    public  RequestContextAdd(EnrollmentRequestDTO enrollmentRequestDTO) {
        this.enrollmentRequestDTO = enrollmentRequestDTO;
    }
}
