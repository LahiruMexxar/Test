package com.Mexxar.Test.Controller;

import com.Mexxar.Test.DTO.ApiResponse;
import com.Mexxar.Test.Model.Department;
import com.Mexxar.Test.Repository.DepartmentRepository;
import com.Mexxar.Test.Service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.text.Bidi;
import java.util.*;

@RestController
@RequestMapping("/api/v1/department")
@PreAuthorize("hasRole('ADMIN')")
public class DepartmentController {
    @Autowired
    private DepartmentRepository departmentRepository;
    @Autowired
    private DepartmentService departmentService;

    //Used to Handle Field Validation
    private ResponseEntity<?> handleValidationErrors(BindingResult bindingResult){
        Map<String,String> errors = new HashMap<>();
        for (FieldError error : bindingResult.getFieldErrors()){
            errors.put(error.getField(),error.getDefaultMessage());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    //Get Department (Method called in service.java)
    @GetMapping
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<List<Department>>> getAllDepartments(){
        try{
            List<Department> departments = departmentService.getAllDepartments();
            return ResponseEntity.ok(new ApiResponse<>(200,"Departments Retrieved",departments));
        }catch (Exception e){
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>(500,"Internal Server Error",new ArrayList<>()));
        }
    }
    //Get Department By Id(Method called in service.java)
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponse<Department>> getDepartmentById(@PathVariable Long id){
        try {
            Optional<Department> departmentOptional = departmentService.getDepartmentById(id);
            if (departmentOptional.isPresent()) {
                return ResponseEntity.ok(new ApiResponse<>(200, "Department Retrieved By ID", departmentOptional.get()));
            } else {
                return ResponseEntity.status(404).body(new ApiResponse<>(404, "Department not found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "Internal Server Error", null));
        }
    }

    //Create Department
    @PostMapping
    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<ApiResponse<List<Department>>> createDepartment(@Valid @RequestBody Department department , BindingResult result){
        try {
            if(result.hasErrors()){//check for field validation
                return ResponseEntity.badRequest().body(new ApiResponse<>(400,"Please Fill All Fields", new ArrayList<>()));
            }
            return departmentService.saveDepartment(department);
        } catch (Exception e){
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "Internal Server Error", new ArrayList<>()));
        }
    }
    //Update Department
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponse<List<Department>>> updateDepartment(@Valid @RequestBody Department updateddepartment, @PathVariable Long id , BindingResult result) {
        try {
            if (result.hasErrors()) {//check for field validation
                return ResponseEntity.badRequest().body(new ApiResponse<>(400, "Please Fill All Fields", new ArrayList<>()));
            }
            return departmentService.updateDepartment(id, updateddepartment);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "Internal Server Error", new ArrayList<>()));
        }
    }
    //Delete Department
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponse<List<Void>>> deleteDepartment(@PathVariable Long id){
        try {
            return departmentService.deleteDepartment(id);
        }catch (Exception e){
            return ResponseEntity.status(500).body(new ApiResponse<>(500, "Internal Server Error", new ArrayList<>()));
        }
    }

}
