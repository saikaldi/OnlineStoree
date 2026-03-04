package com.example.onlineStore.controller;

import com.example.onlineStore.model.Category;
import com.example.onlineStore.repo.CategoryRepository;
import com.example.onlineStore.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/categories")
@RestController
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id){
        Category category=categoryService.findById(id);
        return ResponseEntity.ok(category);
    }
    @GetMapping("/")
    public ResponseEntity<List<Category>> getAllCategories(){
        return ResponseEntity.ok(categoryService.allCategories());
    }
    @PostMapping("/")
    public ResponseEntity<Category> addCategory(@RequestBody Category category){
        return ResponseEntity.status(201).body(categoryService.addCategory(category));
    }
    @PutMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id,@RequestBody Category category){
        return ResponseEntity.ok(categoryService.updateCategory(id, category));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeCategory(@PathVariable Long id){
        categoryService.deleteCategoryById(id);
        return ResponseEntity.ok("Category deleted successfully");

    }
}
