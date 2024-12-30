package com.example.employee_management.controller;

import com.example.employee_management.entity.Employee;
import com.example.employee_management.repository.EmployeeDao;
import com.example.employee_management.service.EmployeeServiceDaoImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeServiceDaoImpl employeeServiceDaoImpl;

    @PostMapping("/add")
    public String addEmployee(@Valid @RequestBody Employee employee) {
        boolean isSuccess = employeeServiceDaoImpl.add(employee);
        return isSuccess ? "Employee added successfully" : "Failed to add employee";
    }

    @PutMapping("/update/{id}")
    public String updateEmployee(@PathVariable int id, @RequestBody Employee employee) {
       employeeServiceDaoImpl.update(id, employee);
       return "Employee updated successfully";

    }



    @DeleteMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable int id) {
        boolean isSuccess = employeeServiceDaoImpl.delete(id);
        return isSuccess ? "Employee deleted successfully" : "Failed to delete employee";
    }

    @GetMapping("/find/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") int id) {
        Optional<Employee> employeeOptional = employeeServiceDaoImpl.getById(id);

        if (employeeOptional.isPresent()) {
            return new ResponseEntity<>(employeeOptional.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeServiceDaoImpl.getAll();
    }
}
