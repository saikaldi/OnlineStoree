package com.example.onlineStore.controller;

import com.example.onlineStore.model.Product;
import com.example.onlineStore.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/products")
@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @PostMapping
    public ResponseEntity<Product> addProduct(
            @RequestBody Product product,
            @RequestParam Long categoryId) {

        Product savedProduct = productService.addProduct(product, categoryId);
        return ResponseEntity.status(201).body(savedProduct);
    }
    @GetMapping("/")
    public ResponseEntity<List<Product>> getAllProducts(){
        return ResponseEntity.ok(productService.gelAllProducts());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id){
        Product product=productService.findProductById(id);
        return ResponseEntity.ok(product);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product,
            @RequestParam Long categoryId) {

        Product updatedProduct = productService.updateProduct(id, product, categoryId);
        return ResponseEntity.ok(updatedProduct);
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product with ID " + id + " has been deleted successfully.");

    }
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER')")
//    @PreAuthorize("isAuthenticated()")
    @GetMapping("/category/{categoryId}")
    public List<Product> getProductsByCategory(@PathVariable Long categoryId) {
        System.out.println("DEBUG: Searching products for category ID: " + categoryId);
        return productService.getProductsByCategory(categoryId);
    }

}
