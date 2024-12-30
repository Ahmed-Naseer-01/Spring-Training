package com.example.expense_reimbursement_system.service;


import com.example.expense_reimbursement_system.entity.RoleCategoryPackage;
import com.example.expense_reimbursement_system.repository.RoleCategoryPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleCategoryPackageService {
    @Autowired
    private RoleCategoryPackageRepository roleCategoryPackageRepository;

    public RoleCategoryPackage addRoleCategoryPackage(RoleCategoryPackage roleCategoryPackage) {
        return roleCategoryPackageRepository.save(roleCategoryPackage);
    }
}
