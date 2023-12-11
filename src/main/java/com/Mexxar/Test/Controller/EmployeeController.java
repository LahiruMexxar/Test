package com.Mexxar.Test.Controller;

import com.Mexxar.Test.DTO.ApiResponse;
import com.Mexxar.Test.Model.Department;
import com.Mexxar.Test.Model.Employee;
import com.Mexxar.Test.Model.EmployeewithDeptRequest;
import com.Mexxar.Test.Repository.DepartmentRepository;
import com.Mexxar.Test.Repository.EmployeeRepository;
import com.Mexxar.Test.Service.EmployeeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/employee")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class EmployeeController {

    @Autowired
    EmployeeRepository employeeRepository;
    @Autowired
    DepartmentRepository departmentRepository;
    @Autowired
    EmployeeService employeeService;

    //Used to Handle Field Validation
    private ResponseEntity<?> handleValidationErrors(BindingResult bindingResult){
        Map<String,String> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()){
            errors.put(error.getField(),error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    //Get Employees (Method called in service.java)
    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<Employee>>> getAllEmployees(){
        try{
            List<Employee> departments = employeeService.getAllEmployees();
            return ResponseEntity.ok(new ApiResponse<>(200,"Employees Retrieved",departments));
        }catch (Exception e){
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>(500,"Internal Server Error",new ArrayList<>()));
        }
    }
    //Get Employee By Id(Method called in service.java)
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<Employee>> getDepartmentById(@PathVariable Long id){
        try {
            Optional<Employee> employeeOptional = employeeService.getEmployeeById(id);
            if (employeeOptional.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(200, "Employee Retrieved By ID", employeeOptional.get()));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(404, "Employee not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "Internal Server Error", null));
        }
    }
    //Create Employees in Department
    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<ApiResponse<List<Employee>>> createDepartment(@Valid @RequestBody Employee employee , EmployeewithDeptRequest request, BindingResult result){
        try {
            if(result.hasErrors()){//check for field validation
                return ResponseEntity.badRequest().body(new ApiResponse<>(400,"Please Fill All Fields", new ArrayList<>()));
            }
            return employeeService.saveEmployees(employee,request);
        } catch (Exception e){
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "Internal Server Error", new ArrayList<>()));
        }
    }
}
