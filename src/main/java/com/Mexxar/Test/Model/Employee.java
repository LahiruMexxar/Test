package com.Mexxar.Test.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long empid;
    @NotBlank
    private String firstname;
    @NotBlank
    private String lastname;
    @NotBlank
    private int contactnum;
    @NotBlank
    private String email;
    @NotBlank
    private String gender;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    public Employee(Employee employee) {
    }
}
