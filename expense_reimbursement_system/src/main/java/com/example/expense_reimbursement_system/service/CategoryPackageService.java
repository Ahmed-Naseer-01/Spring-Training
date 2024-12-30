package com.example.expense_reimbursement_system.service;


import com.example.expense_reimbursement_system.entity.CategoryPackage;
import com.example.expense_reimbursement_system.repository.CategoryPackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryPackageService {

@Autowired
private CategoryPackageRepository categoryPackageRepository;

    // add Package for category
    public CategoryPackage addCategoryPackage(CategoryPackage categoryPackage) {
        return categoryPackageRepository.save(categoryPackage);
    }
    // Get all the Category Packages
    public List<CategoryPackage> getAllCategoryPackages() {
    return categoryPackageRepository.findAll();
    }

    public CategoryPackage getCategoryPackageByName(String name) {
        return categoryPackageRepository.findByPackageName(name);
    }

}
