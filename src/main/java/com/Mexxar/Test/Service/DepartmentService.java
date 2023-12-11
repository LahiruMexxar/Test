package com.Mexxar.Test.Service;

import com.Mexxar.Test.DTO.ApiResponse;
import com.Mexxar.Test.Model.Department;
import com.Mexxar.Test.Repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    //get all departments in a list
    public List<Department> getAllDepartments(){
        return departmentRepository.findAll();
    }

    //get departments by ID
    public Optional<Department> getDepartmentById(Long id){
        return departmentRepository.findById(id);
    }

    //Create Department
    public ResponseEntity<ApiResponse<List<Department>>> saveDepartment(Department department){
        try{ //Check if Department name already on the database
            if(isProductDuplicate(department.getName())){
               return ResponseEntity.badRequest()
                       .body(new ApiResponse<>(200,"Department Name Already Exists",new ArrayList<>()));
            }
            //Save Department if the name is not duplicated
            departmentRepository.save(department);
            return ResponseEntity //Adding meaningful error message
                    .ok(new ApiResponse<>(201,"Department Added Successfully",new ArrayList<>()));
        }catch (Exception e){
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>(500,"Internal Server Error",new ArrayList<>()));
        }
    }
   //Update Department
    public ResponseEntity<ApiResponse<List<Department>>> updateDepartment(Long id , Department department){
        try {
            Optional<Department> existingDeptOptional = departmentRepository.findById(id); //assigned method to find dept by Id
            //Check if the Department is in the database
            if(existingDeptOptional.isPresent()){
                Department existingDept = existingDeptOptional.get();
                // if present set new data
                existingDept.setName(department.getName());
                //save new data
                departmentRepository.save(existingDept);
                //Adding meaningful error message
                return ResponseEntity.ok(new ApiResponse<>(200,"Department Updated",new ArrayList<>()));
            }else{// if Department is not present
                return ResponseEntity.status(404)
                        .body(new ApiResponse<>(404,"Department not found",new ArrayList<>()));
            }
        }catch (Exception e){
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>(500,"Internal Server Error",new ArrayList<>()));
        }
    }
    //Delete Department
    public ResponseEntity<ApiResponse<List<Void>>> deleteDepartment(Long id) {
        try {
            //Check if the Department exist in the database
            Optional<Department> exisitingProductOptional = departmentRepository.findById(id);
            if (exisitingProductOptional.isPresent()) {
                Department department = exisitingProductOptional.get();
                //if Department is present then delete
                departmentRepository.deleteById(id);
                //Adding meaningful error message
                return ResponseEntity.ok(new ApiResponse<>(200, "Department Deleted", new ArrayList<>()));
            } else {// if Department is not present
                return ResponseEntity.status(404)
                        .body(new ApiResponse<>(404, "Department not found", new ArrayList<>()));
            }

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new ApiResponse<>(500, "Internal Server Error", new ArrayList<>()));
        }
    }
    //Method to check if Deptname is found in the database
    private boolean isProductDuplicate(String departmentName) {
        return departmentRepository.existsByName(departmentName);
    }
}
