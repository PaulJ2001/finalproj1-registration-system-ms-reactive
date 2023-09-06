package com.champlain.sectionsservice.presentationlayer;



import com.champlain.sectionsservice.dataaccesslayer.Semester;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponseDTO {

    private  String enrollmentId;
    private Integer enrollmentYear;
    private Semester semester;
    private String studentId;
    private String studentFirstName;
    private String studentLastName;
    private String courseId;
    private String courseNumber;
    private String courseName;
}
