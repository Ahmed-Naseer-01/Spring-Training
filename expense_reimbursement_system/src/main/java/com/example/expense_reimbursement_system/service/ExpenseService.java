package com.example.expense_reimbursement_system.service;


import com.example.expense_reimbursement_system.entity.*;
import com.example.expense_reimbursement_system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final EmployeeRepository employeeRepository;
    private final CategoriesRepository categoriesRepository;
    private final ExpenseStatusRepository expenseStatusRepository;

    @Autowired
    public ExpenseService(ExpenseRepository expenseRepository, ExpenseStatusRepository expenseStatusRepository, CategoriesRepository categoriesRepository, EmployeeRepository employeeRepository) {
        this.expenseRepository = expenseRepository;
        this.employeeRepository = employeeRepository;
        this.categoriesRepository = categoriesRepository;
        this.expenseStatusRepository = expenseStatusRepository;
    }


    // create Employee expense request ticket
    public Expense createExpense(int employeeId, Expense expense) {
        // Fetch the employee from the repository using the employeeId
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));
        // Set the submit date
        expense.setSubmitDate(new Date());
        // Set approved date to null by default
        expense.setApprovalDate(null);
        // Set the employee in the expense
        expense.setEmployee(employee);

        ExpenseStatus status = new ExpenseStatus();
        status.setName("Pending"); // Default status
        status.setStatus(true); // default active flag
        status = expenseStatusRepository.save(status);
        // Link the saved status to the expense
        expense.setStatus(status);
        // Save and return the expense
        return expenseRepository.save(expense);
    }

    // Get expenses by status
    public List<Expense> getExpensesByStatus(String statusName) {
        return expenseRepository.findByStatus_Name(statusName);
    }

    public void updateExpenseStatusByExpenseId(int expenseId, String name) {
        // get the expense by its Id
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found with ID: " + expenseId));
        // Get the associated status
        ExpenseStatus expenseStatus = expense.getStatus();

        if (name != null && !name.isEmpty()) {
            // Update the status name
            expenseStatus.setName(name);
            expenseStatusRepository.save(expenseStatus);
        }
        // Update the approval date if needed
        expense.setApprovalDate(new Date());
        expenseRepository.save(expense);
    }

    // Get expenses by employee within a date range
    public List<Map<String, Object>> getEmployeeExpenses(int employeeId, String startDate, String endDate) {
        LocalDateTime start = startDate != null ? LocalDateTime.parse(startDate) : LocalDateTime.MIN;
        LocalDateTime end = endDate != null ? LocalDateTime.parse(endDate) : LocalDateTime.MAX;

        return expenseRepository.findByEmployeeIdAndDateRange(employeeId, start, end)
                .stream()
                .map(expense -> {
                    Map<String, Object> expenseData = new HashMap<>();
                    expenseData.put("id", expense.getId());
                    expenseData.put("description", expense.getDescription());
                    expenseData.put("submitDate", expense.getSubmitDate());
                    expenseData.put("status", expense.getStatus().getName());
                    return expenseData;
                })
                .toList();
    }
}
