package com.example.employee_management.controller;

import com.example.employee_management.entity.Employee;
import com.example.employee_management.repository.EmployeeDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class EmployeeController {

    @Autowired
    private EmployeeDao employeeDao;

    @PostMapping("/addEmployee")
    public String addEmployee(@RequestBody Employee employee) {
        boolean isSuccess = employeeDao.insertEmployee(employee);
        return isSuccess ? "Employee added successfully" : "Failed to add employee";
    }


    @DeleteMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable int id) {
        boolean isSuccess = employeeDao.deleteEmployeeById(id);
        return isSuccess ? "Employee deleted successfully" : "Failed to delete employee";
    }

    @GetMapping("/findEmployee/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") int id) {
        try {
            Employee employee = employeeDao.findEmployeeById(id);
            return new ResponseEntity<>(employee, HttpStatus.OK);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // Employee not found
        }
    }

    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeDao.getAllEmployees();
    }
}
