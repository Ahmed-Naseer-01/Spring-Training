package com.example.expense_reimbursement_system.service;


import com.example.expense_reimbursement_system.entity.Role;
import com.example.expense_reimbursement_system.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    // Create Role
    public Role createRole(Role role) {
        Optional<Role> existingRole = roleRepository.findByName(role.getName().toLowerCase());
        if (existingRole.isPresent()) {
            throw new IllegalArgumentException("Role with name " + role.getName() + " already exists.");
        }
        role.setName(role.getName().toLowerCase());
        role.setStatus(true);
        return roleRepository.save(role);
    }

    // Get all Roles
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    // Set Role Inactive
    public void inactiveRole(Role role) {
        role.setStatus(false);
        roleRepository.save(role);
    }

    // Get Role by its Id
    public Role getRoleById(int id) {
        return roleRepository.findById(id).get();
    }

    // Delete Role by its Id
    public void deleteRoleById(int id) {
        roleRepository.deleteById(id);
    }
}
