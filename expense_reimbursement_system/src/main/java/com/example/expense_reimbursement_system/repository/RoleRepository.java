package com.example.expense_reimbursement_system.repository;


import com.example.expense_reimbursement_system.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    // Get Role by name
    Optional<Role> findByName(String name);
}
