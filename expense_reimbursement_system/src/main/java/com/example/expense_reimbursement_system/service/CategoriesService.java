package com.example.expense_reimbursement_system.service;


import com.example.expense_reimbursement_system.entity.Categories;
import com.example.expense_reimbursement_system.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;

@Service
public class CategoriesService {

    private CategoriesRepository categoriesRepository;

    @Autowired
    public CategoriesService(CategoriesRepository categoriesRepository) {
        this.categoriesRepository = categoriesRepository;
    }

    public List<Categories> getAllCategories() {
        return categoriesRepository.findAll();
    }

    public Categories getCategoriesById(int id) {
        return categoriesRepository.findById(id).get();
    }

    @Transactional
    public Categories createCategories(Categories categories) {
        try {
            // set categories status true as by default
            categories.setStatus(true);
            // Save the categories entity
           return  categoriesRepository.save(categories);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save category: " + e.getMessage(), e);
        }
    }
}
