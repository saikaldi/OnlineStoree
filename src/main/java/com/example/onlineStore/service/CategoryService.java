package com.example.onlineStore.service;


import com.example.onlineStore.model.Category;
import com.example.onlineStore.repo.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public List<Category> allCategories(){
        return categoryRepository.findAll();
    }

    public Category addCategory(Category category){
        return categoryRepository.save(category);
    }

    public Category updateCategory(Long id, Category categoryDetails){
        Category category=categoryRepository.findById(id)
            .orElseThrow(()-> new RuntimeException("Category not found with id:" + id));
        category.setName(categoryDetails.getName());
        category.setDescription(categoryDetails.getDescription());

        return categoryRepository.save(category);
    }

    public void deleteCategoryById(Long id){
        if(!categoryRepository.existsById(id)){
            throw new RuntimeException("Category not found with id:" + id);
        }
        categoryRepository.deleteById(id);
    }

    public Category findCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Category not found with id:" + id));

    }
}
