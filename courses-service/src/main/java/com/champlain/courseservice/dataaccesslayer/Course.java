package com.champlain.courseservice.dataaccesslayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Course {



    private String Id;
    private String courseId;
    private String courseNumber;
    private String courseName;
    private int numHours;
    private double numCredits;
    private String department;

}
