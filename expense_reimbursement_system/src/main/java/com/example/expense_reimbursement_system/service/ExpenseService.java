package com.example.expense_reimbursement_system.service;


import com.example.expense_reimbursement_system.entity.*;
import com.example.expense_reimbursement_system.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class ExpenseService {

    @Autowired
    private  ExpenseRepository expenseRepository;
    @Autowired
    private  EmployeeRepository employeeRepository;
    @Autowired
    private  CategoriesRepository categoriesRepository;
    @Autowired
    private  ExpenseStatusRepository expenseStatusRepository;
    @Autowired
    private RoleCategoryPackageRepository roleCategoryPackageRepository;


//    helper function
private RoleCategoryPackage getRoleCategoryPackageForEmployee(int employeeId) {
    Employee employee = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

    Role role = employee.getRole();
    if (role == null) {
        throw new RuntimeException("Role not found for the employee: " + employee.getId());
    }

    RoleCategoryPackage roleCategoryPackage = roleCategoryPackageRepository.findByRole(role)
            .orElseThrow(() -> new RuntimeException("RoleCategoryPackage not found for role ID: " + role.getId()));

    CategoryPackage categoryPackage = roleCategoryPackage.getCategoryPackage();
    if (categoryPackage == null) {
        throw new RuntimeException("CategoryPackage not found for RoleCategoryPackage ID: " + roleCategoryPackage.getId());
    }

    return roleCategoryPackage;
}

    @Transactional
    // create Employee expense request ticket
    public Expense createExpense(int employeeId, Expense expense) {


        // Fetch the employee from the repository using the employeeId
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        if (isValidateAmount(employeeId, expense.getAmount())) {
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
        throw new IllegalArgumentException("Invalid expense data.");
    }

    // Get expenses by status
    public List<Expense> getExpensesByStatus(String statusName) {
        return expenseRepository.findByStatus_Name(statusName);
    }


    public boolean isValidateAmount(int employeeId, int amount) {
        // Get the RoleCategoryPackage associated with the employee
        RoleCategoryPackage roleCategoryPackage = getRoleCategoryPackageForEmployee(employeeId);

        // Retrieve the expense limit for the category package
        int maxExpenseLimit = roleCategoryPackage.getCategoryPackage().getExpenseLimit();

        return amount <= maxExpenseLimit;
    }

    public boolean validateExpense(int expenseId) {
        // Fetch the expense by ID
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new RuntimeException("Expense not found with ID: " + expenseId));

        // Get the employee who made the expense
        Employee employee = expense.getEmployee();
        if (employee == null) {
            throw new RuntimeException("Employee not found for the expense ID: " + expenseId);
        }

        // Get the RoleCategoryPackage associated with the employee
        RoleCategoryPackage roleCategoryPackage = getRoleCategoryPackageForEmployee(employee.getId());

        // Retrieve the expense limit for the category package
        int maxExpenseLimit = roleCategoryPackage.getCategoryPackage().getExpenseLimit();

        // Calculate the sum of all approved expenses for this employee and category
        int totalApprovedExpenses = expenseRepository.findTotalApprovedExpensesByEmployeeAndCategory(employee, roleCategoryPackage.getCategoryPackage().getCategory());

        // Calculate the remaining amount
        int remainingAmount = maxExpenseLimit - totalApprovedExpenses;

        // Check if the current expense amount is within the remaining limit
        return expense.getAmount() <= remainingAmount;
    }

    public void updateExpenseStatusByExpenseId(int expenseId, String name) {

        if (validateExpense(expenseId)) {
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
        } else {
            throw new RuntimeException("Expense " + expenseId + " can not be updated");
        }
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
