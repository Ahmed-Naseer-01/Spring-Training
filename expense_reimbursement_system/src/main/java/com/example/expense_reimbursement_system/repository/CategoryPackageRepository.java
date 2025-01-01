package com.example.expense_reimbursement_system.repository;


import com.example.expense_reimbursement_system.entity.CategoryPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryPackageRepository extends JpaRepository<CategoryPackage, Integer> {
    CategoryPackage findByPackageName(String packageName);

    @Query("SELECT cp.expenseLimit FROM CategoryPackage cp " +
            "JOIN RoleCategoryPackage rcp ON cp.id = rcp.categoryPackage.id " +
            "WHERE rcp.role.id = :roleId AND cp.category.id = :categoryId")
    int findExpenseLimitByRoleAndCategory(@Param("roleId") int roleId, @Param("categoryId") int categoryId);
}
