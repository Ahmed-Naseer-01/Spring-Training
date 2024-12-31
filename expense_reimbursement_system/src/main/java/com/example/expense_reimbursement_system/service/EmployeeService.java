package com.example.expense_reimbursement_system.service;


import com.example.expense_reimbursement_system.entity.Employee;
import com.example.expense_reimbursement_system.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional
    public Employee createEmployee(Employee employee) {
        try {
            return employeeRepository.save(employee);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save the employee: " + e.getMessage(), e);
        }
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }
}
