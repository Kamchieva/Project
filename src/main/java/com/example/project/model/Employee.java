package com.example.project.model;

import jakarta.persistence.*; // Or javax.persistence.* for older versions

@Entity
@Table(name = "Employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Integer age;

    private String department;

    // Default constructor is required by JPA
    public Employee() {
    }

    // Constructor to create an Employee instance
    public Employee(String name, Integer age, String department) {
        this.name = name;
        this.age = age;
        this.department = department;
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}

