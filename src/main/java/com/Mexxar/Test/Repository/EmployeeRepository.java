package com.Mexxar.Test.Repository;

import com.Mexxar.Test.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface EmployeeRepository extends JpaRepository<Employee,Long> {
    boolean existsByEmail(String email);
}
