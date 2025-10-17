package com.example.project.repository;

import com.example.project.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    // Spring will automatically provide implementations for standard CRUD methods, such as:
    // save(), findById(), findAll(), deleteById()

    // You can also define custom query methods here. For example:
     List<Employee> findByDepartment(String department);
     List<Employee> findByAgeGreaterThan(Integer age);
}
