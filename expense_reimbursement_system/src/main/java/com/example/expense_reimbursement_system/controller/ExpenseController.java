package com.example.expense_reimbursement_system.controller;

import com.example.expense_reimbursement_system.entity.Expense;
import com.example.expense_reimbursement_system.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @Autowired
    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

//     POST: Create Expense
    @PostMapping("/add/{employeeId}")
    public ResponseEntity<?> addExpense(@PathVariable int employeeId, @RequestBody Expense expense) {
        try {
            // Call the service method to create the expense
            Expense createdExpense = expenseService.createExpense(employeeId, expense);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdExpense);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to add expense: " + e.getMessage());
        }
    }

    // GET: Get Expenses by Status
    @GetMapping("/status/{statusId}")
    public ResponseEntity<?> getExpensesByStatus(@PathVariable int statusId) {
        try {
            List<Expense> expenses = expenseService.getExpensesByStatus(statusId);
            if (expenses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No expenses found for status id: " + statusId);
            }
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to fetch expenses: " + e.getMessage());
        }
    }

    // update status by Manager(assumption)
    @PostMapping("/handleStatus")
    public ResponseEntity<String> updateExpenseStatus(@RequestBody Map<String, Object> payload) {
        try {
            int expenseId = (int) payload.get("expenseId");
            int statusId = (int) payload.get("statusId");

            expenseService.updateExpenseStatusByExpenseId(expenseId, statusId);
            return ResponseEntity.ok("Expense status updated successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An unexpected error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<List<Map<String, Object>>> getStatusByEmployee(@RequestBody Map<String, Object> payload) {
        try {
            int employeeId = (int) payload.get("employeeId");
            String startDate = (String) payload.getOrDefault("startDate", null);
            String endDate = (String) payload.getOrDefault("endDate", null);

            List<Map<String, Object>> expenses = expenseService.getEmployeeExpenses(employeeId, startDate, endDate);
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
