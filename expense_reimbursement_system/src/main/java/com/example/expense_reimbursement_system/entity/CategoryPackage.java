package com.example.expense_reimbursement_system.entity;

import com.example.expense_reimbursement_system.entity.Categories;
import jakarta.persistence.*;

@Entity
public class CategoryPackage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Categories category;

    @Column(nullable = false, length = 50)
    private String packageName;

    @Column(nullable = false)
    private int expenseLimit;

    // default constructor
    public CategoryPackage() {}

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Categories getCategory() {
        return category;
    }

    public void setCategory(Categories category) {
        this.category = category;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getExpenseLimit() {
        return expenseLimit;
    }

    public void setExpenseLimit(Integer expenseLimit) {
        this.expenseLimit = expenseLimit;
    }
}