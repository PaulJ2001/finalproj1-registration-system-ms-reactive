package com.champlain.courseservice.presentationlayer;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseRequestDTO {

    private String courseNumber;
    private String courseName;
    private Integer numHours; //
    private Double numCredits;
    private String department;
}
