package com.Mexxar.Test.Model;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeewithDeptRequest {
    private String firstname;
    @NotBlank
    private String lastname;
    @NotBlank
    private int contactnum;
    @NotBlank
    private String email;
    @NotBlank
    private String gender;
    @NotBlank
    private String name;
}
