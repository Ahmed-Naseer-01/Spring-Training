package com.example.expense_reimbursement_system.entity;

import jakarta.persistence.*;

@Entity
public class ExpenseStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private boolean status;

    @OneToOne(mappedBy = "status", cascade = CascadeType.ALL)
    private Expense expense;

    public ExpenseStatus() {}

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isStatus() {
        return status;
    }
}
