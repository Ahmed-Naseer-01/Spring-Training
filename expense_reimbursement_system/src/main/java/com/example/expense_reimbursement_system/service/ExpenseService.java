package com.example.expense_reimbursement_system.service;


import com.example.expense_reimbursement_system.entity.*;
import com.example.expense_reimbursement_system.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
    @Autowired
    private CategoryPackageRepository categoryPackageRepository;


@Transactional
public ResponseEntity<Expense> createExpense(int employeeId, Expense expense) {
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

    // Validate request amount
    if (isValidateAmount(employee.getRole().getId(), expense) && validateExpense(employeeId, expense)) {
        // Set status to "Approved"
        ExpenseStatus status = expenseStatusRepository.findById(1)
                .orElseThrow(() -> new EntityNotFoundException("Expense Status not found with ID: 1"));

        expense.setStatus(status);
        // Save and return the expense with "Approved" status
        Expense savedExpense = expenseRepository.save(expense);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedExpense); // 201 Created
    } else {
        // Set status to "Rejected"
        ExpenseStatus status = expenseStatusRepository.findById(3)
                .orElseThrow(() -> new EntityNotFoundException("Expense Status not found with ID: 3"));

        expense.setStatus(status);
        // Save the expense with Rejected status in the database
        Expense savedExpense = expenseRepository.save(expense);

        // Return the response with a 400 Bad Request status and the rejected expense details
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(savedExpense); // 400 Bad Request
    }
}
    // Get expenses by status
    public List<Expense> getExpensesByStatus(int statusId) {
        return expenseRepository.findByStatus_id(statusId);
    }

    public boolean isValidateAmount(int roleId, Expense expense) {

    // Get the expense limit (amount) for the given role and category
        int maxExpenseLimit = categoryPackageRepository.findExpenseLimitByRoleAndCategory(roleId, expense.getCategory().getId());

        // Return true if the amount is within the limit
        return expense.getAmount() <= maxExpenseLimit;
    }

    public boolean validateExpense(int employeeId, Expense expense) {

        // Fetch the employee from the repository using the employeeId
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        int maxExpenseLimit = categoryPackageRepository.findExpenseLimitByRoleAndCategory(employee.getRole().getId(), expense.getCategory().getId());

        // Calculate the sum of all approved expenses for this employee and category
        int totalApprovedExpenses = expenseRepository.findApprovedExpensesAmountByEmployeeAndCategory(employee, expense.getCategory());

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
