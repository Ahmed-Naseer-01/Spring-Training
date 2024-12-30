package com.example.expense_reimbursement_system.controller;


import com.example.expense_reimbursement_system.entity.RoleCategoryPackage;
import com.example.expense_reimbursement_system.service.RoleCategoryPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/RoleCatPackages")
public class RoleCategoryPackageController {
    @Autowired
    private RoleCategoryPackageService roleCategoryPackageService;

    @PostMapping("/add")
    public ResponseEntity<RoleCategoryPackage> addRoleCategoryPackage(@RequestBody RoleCategoryPackage roleCategoryPackage) {
        RoleCategoryPackage savedPackage = roleCategoryPackageService.addRoleCategoryPackage(roleCategoryPackage);
        return ResponseEntity.ok(savedPackage);
    }
}
