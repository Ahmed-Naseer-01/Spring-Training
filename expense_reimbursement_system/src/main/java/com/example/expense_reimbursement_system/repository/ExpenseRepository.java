package com.example.expense_reimbursement_system.repository;


import com.example.expense_reimbursement_system.entity.Expense;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, Integer> {

    // Get status by it's name(status)
    List<Expense> findByStatus_Name(String statusName);

    // Get Expenses by Employee or by Date Range
    @Query("SELECT e FROM Expense e WHERE e.employee.id = :employeeId AND e.submitDate BETWEEN :start AND :end")
    List<Expense> findByEmployeeIdAndDateRange(int employeeId, LocalDateTime start, LocalDateTime end);
}
