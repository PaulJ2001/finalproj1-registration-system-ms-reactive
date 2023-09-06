package com.champlain.sectionsservice.buisnesslayer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponseDTO {


    private String studentId;
    private String firstName;
    private String lastName;
    private String program;
}
