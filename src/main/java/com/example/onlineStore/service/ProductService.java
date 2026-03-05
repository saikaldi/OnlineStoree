package com.example.onlineStore.service;

import com.example.onlineStore.model.Category;
import com.example.onlineStore.model.Product;
import com.example.onlineStore.repo.CategoryRepository;
import com.example.onlineStore.repo.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public ProductService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product, Long category_id){
        Category category = categoryRepository.findById(category_id)
                .orElseThrow(()->new RuntimeException("Category not found with id: " + category_id));

        product.setCategory(category);
        return productRepository.save(product);
    }

    public List<Product> gelAllProducts() {
        return productRepository.findAll();
    }

    public Product findProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Product not found with id:" + id));
    }

    public Product updateProduct(Long id, Product productDetails, Long categoryId) {
        Product product =productRepository.findById(id)
                .orElseThrow(()->new RuntimeException("Product not found with id: " + id));
        Category category=categoryRepository.findById(categoryId)
                .orElseThrow(()->new RuntimeException("Category not found with id: " + categoryId));
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStockQuantity(productDetails.getStockQuantity());
        product.setCategory(category);

        return productRepository.save(product);

    }
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
    public List<Product> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategory_Id(categoryId);
    }
}
