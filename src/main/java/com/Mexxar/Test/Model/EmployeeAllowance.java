package com.Mexxar.Test.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmployeeAllowance {

    @OneToMany
    @JoinColumn(name = "employee_id")
    private List<Employee> employee;
    private String month;
    private double allowance;
    private Date date;
    private String status;
}
