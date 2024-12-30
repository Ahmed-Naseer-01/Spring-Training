package com.example.expense_reimbursement_system.repository;


import com.example.expense_reimbursement_system.entity.CategoryPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryPackageRepository extends JpaRepository<CategoryPackage, Integer> {
    public CategoryPackage findByPackageName(String packageName);
}
