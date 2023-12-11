package com.Mexxar.Test.Model;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class DepartmentwithEmpRequest {
    private Long departmentId;
    private String departmentName;
    private List<Employee> employees;

    public DepartmentwithEmpRequest(Department department, List<Employee> employees) {
        this.departmentId = department.getDeptid();
        this.departmentName = department.getName();
        this.employees = employees.stream()
                .map(Employee::new)
                .collect(Collectors.toList());
    }

}
