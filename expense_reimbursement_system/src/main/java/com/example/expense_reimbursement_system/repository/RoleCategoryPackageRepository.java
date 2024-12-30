package com.example.expense_reimbursement_system.repository;

import com.example.expense_reimbursement_system.entity.Role;
import com.example.expense_reimbursement_system.entity.RoleCategoryPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleCategoryPackageRepository extends JpaRepository<RoleCategoryPackage, Integer> {

    Optional<RoleCategoryPackage> findByRole(Role role);
}
