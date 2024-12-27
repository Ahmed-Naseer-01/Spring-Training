package com.example.expense_reimbursement_system.service;


import com.example.expense_reimbursement_system.entity.ExpenseStatus;
import com.example.expense_reimbursement_system.repository.ExpenseStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExpenseStatusService {
    private final ExpenseStatusRepository expenseStatusRepository;
    public ExpenseStatusService(ExpenseStatusRepository expenseStatusRepository) {
        this.expenseStatusRepository = expenseStatusRepository;
    }

    @Transactional
    public ExpenseStatus createExpenseStatus(ExpenseStatus expenseStatus) {
        try {

            expenseStatus.setName("Pending"); // Default status
            expenseStatus.setStatus(true); // Default active
            // Save the categories entity
            return  expenseStatusRepository.save(expenseStatus);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save Expense Status: " + e.getMessage(), e);
        }
    }
}
