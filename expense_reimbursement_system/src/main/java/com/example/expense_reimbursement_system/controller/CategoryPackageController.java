package com.example.expense_reimbursement_system.controller;

import com.example.expense_reimbursement_system.entity.CategoryPackage;
import com.example.expense_reimbursement_system.service.CategoryPackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/CategoryPackages")
public class CategoryPackageController {

@Autowired
private CategoryPackageService categoryPackageService;



    @PostMapping("/add")
    public ResponseEntity<CategoryPackage> createCategoryPackage(@RequestBody CategoryPackage categoryPackage) {
        CategoryPackage newcategoryPackage = categoryPackageService.addCategoryPackage(categoryPackage);
        return new ResponseEntity<>(newcategoryPackage, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<List<CategoryPackage>> getAllCategoryPackages() {
        List<CategoryPackage> categoryPackages = categoryPackageService.getAllCategoryPackages();
        return new ResponseEntity<>(categoryPackages, HttpStatus.OK);
    }
}
