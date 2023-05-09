package com.shiva.ecommerce.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shiva.ecommerce.model.Category;
import com.shiva.ecommerce.repository.CategoryRepo;

@Service
public class CategoryService {
    
    @Autowired
    CategoryRepo categoryRepo;

    public List<Category> getCategories(){
        return categoryRepo.findAll();
    }

    public void addCategory(Category category){
        categoryRepo.save(category);
    }

    public void removeCategoryById(int catId) {
        categoryRepo.deleteById(catId);
    }

    public Optional<Category> getCategoryById(int catId) {
        return categoryRepo.findById(catId);
    }
}
