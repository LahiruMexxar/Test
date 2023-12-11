package com.Mexxar.Test.Repository;

import com.Mexxar.Test.Model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface DepartmentRepository extends JpaRepository<Department,Long> {
    boolean existsByName (String name);
}
