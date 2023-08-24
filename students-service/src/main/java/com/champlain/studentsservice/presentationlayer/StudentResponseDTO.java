package com.champlain.studentsservice.presentationlayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponseDTO {

    private String studentId;
    private String firstName;
    private String lastName;
    private String program;


}
