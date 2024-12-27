package com.example.expense_reimbursement_system.repository;

import com.example.expense_reimbursement_system.entity.ExpenseStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExpenseStatusRepository extends JpaRepository<ExpenseStatus, Integer> {
}
