package com.example.expense_reimbursement_system.controller;


import com.example.expense_reimbursement_system.entity.Categories;
import com.example.expense_reimbursement_system.service.CategoriesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoriesController {
    private final CategoriesService categoriesService;
    public CategoriesController(CategoriesService categoriesService) {
        this.categoriesService = categoriesService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addCategory(@RequestBody Categories categories) {
        try {
        Categories savedCategory = categoriesService.createCategories(categories);
            return new ResponseEntity<>( savedCategory, HttpStatus.CREATED);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllCategories() {
        List<Categories> categoriesList = categoriesService.getAllCategories();
        return new ResponseEntity<>(categoriesList, HttpStatus.OK);
    }
}
