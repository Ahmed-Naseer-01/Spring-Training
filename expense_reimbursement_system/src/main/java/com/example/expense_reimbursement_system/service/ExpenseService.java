package com.example.expense_reimbursement_system.service;


import com.example.expense_reimbursement_system.entity.*;
import com.example.expense_reimbursement_system.repository.*;
import jakarta.persistence.EntityNotFoundException;
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
    private CategoriesRepository categoriesRepository;
    @Autowired
    private  EmployeeRepository employeeRepository;
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

        // Set the submit date
        expense.setSubmitDate(LocalDateTime.now());
        // Set approved date to null by default
        expense.setApprovalDate(null);
        // Set the employee in the expense
        expense.setEmployee(employee);

        Categories category = categoriesRepository.findById(expense.getCategory().getId())
                .orElseThrow(() -> new EntityNotFoundException("Category not found with ID: " + expense.getCategory().getId()));

        expense.setCategory(category);

        // validate request amount
        if (isValidateAmount(employeeId, expense.getAmount()) && validateExpense(expense.getId())) {
            ExpenseStatus status = expenseStatusRepository.findById(1)
                    .orElseThrow(() -> new EntityNotFoundException("Expense Status not found with ID: 1"));

            expense.setStatus(status);
            // Save and return the expense
            return expenseRepository.save(expense);
        }
        else {
            ExpenseStatus status = expenseStatusRepository.findById(3)
                    .orElseThrow(() -> new EntityNotFoundException("Expense Status not found with ID: 3"));
            // save expense with rejected status
            expense.setStatus(status);
            // save the expense in the db
            expenseRepository.save(expense);
            throw new IllegalArgumentException("Invalid Claim Amount");
        }
    }

    // Get expenses by status
    public List<Expense> getExpensesByStatus(int statusId) {
        return expenseRepository.findByStatus_id(statusId);
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

    public void updateExpenseStatusByExpenseId(int expenseId, int statusId) {
            // get the expense by its Id
            Expense expense = expenseRepository.findById(expenseId)
                    .orElseThrow(() -> new IllegalArgumentException("Expense not found with ID: " + expenseId));
            // get expense status by it's id
            ExpenseStatus status = expenseStatusRepository.findById(statusId)
                    .orElseThrow(() -> new EntityNotFoundException("Expense Status not found with ID: 1"));
            // Set status
            expense.setStatus(status);
            // Update the approval date if needed
            expense.setApprovalDate(LocalDateTime.now());
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


    // validate expense limit by using
    public boolean validateExpenseLimit(int roleId, int categoryPackageId, int expenseAmount) {
        Optional<RoleCategoryPackage> roleCategoryPackage = roleCategoryPackageRepository.findById(categoryPackageId);
        if (roleCategoryPackage.isPresent() &&
                roleCategoryPackage.get().getRole().getId() == roleId) {
            return expenseAmount <= roleCategoryPackage.get().getCategoryPackage().getExpenseLimit();
        }
        return false;
    }

    }
