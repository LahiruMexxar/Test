package com.Mexxar.Test.Service;

import com.Mexxar.Test.DTO.ApiResponse;
import com.Mexxar.Test.Model.Department;
import com.Mexxar.Test.Model.DepartmentwithEmpRequest;
import com.Mexxar.Test.Model.Employee;
import com.Mexxar.Test.Model.EmployeewithDeptRequest;
import com.Mexxar.Test.Repository.DepartmentRepository;
import com.Mexxar.Test.Repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    //get all employees in a list
    public List<Employee> getAllEmployees(){
        return employeeRepository.findAll();
    }

    //get employee by ID
    public Optional<Employee> getEmployeeById(Long id){
        return employeeRepository.findById(id);
    }

    //Create Employees in Department
    public ResponseEntity<ApiResponse<List<Employee>>> saveEmployees(Employee employee, EmployeewithDeptRequest request){
        try{ //Check if Employee name already on the database
            if(isProductDuplicate(employee.getEmail())){
                return ResponseEntity.badRequest()
                        .body(new ApiResponse<>(200,"Employee Email Already Exists",new ArrayList<>()));
            }
            //Save Employee if the name is not duplicated
            Employee employees = new Employee();
            employees.setFirstname(request.getFirstname());
            employees.setLastname(request.getLastname());
            employees.setContactnum(request.getContactnum());
            employees.setEmail(request.getEmail());
            employees.setGender(request.getGender());

            Department department = new Department();
            department.setName(request.getName());


            employees.setDepartment(department);
            employeeRepository.save(employees);
            departmentRepository.save(department);
            return ResponseEntity //Adding meaningful error message
                    .ok(new ApiResponse<>(201,"Department Added Successfully",new ArrayList<>()));
        }catch (Exception e){
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>(500,"Internal Server Error",new ArrayList<>()));
        }
    }

    private boolean isProductDuplicate(String email) {
        return employeeRepository.existsByEmail(email);
    }
}
