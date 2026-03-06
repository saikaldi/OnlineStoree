package com.example.onlineStore.service;

import com.example.onlineStore.model.CartItem;
import com.example.onlineStore.model.Product;
import com.example.onlineStore.model.User;
import com.example.onlineStore.repo.CartItemRepository;
import com.example.onlineStore.repo.ProductRepository;
import com.example.onlineStore.repo.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartItemService(CartItemRepository cartItemRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public CartItem addItemToCart(Long userId, Long productId, int quantity){
        Optional<CartItem> existingItem=cartItemRepository.findByUserIdAndProductId(userId, productId);

        if(existingItem.isPresent()){
            CartItem item=existingItem.get();
            item.setQuantity(item.getQuantity()+quantity);
            return cartItemRepository.save(item);
        }else{
            User user=userRepository.findById(userId)
                    .orElseThrow(()->new RuntimeException("User not found with this id: " + userId));
            Product product=productRepository.findById(productId)
                    .orElseThrow(()->new RuntimeException("Product not found with this id: " + productId));
            CartItem item=new CartItem();
            item.setUser(user);
            item.setProduct(product);
            item.setQuantity(quantity);
            return cartItemRepository.save(item);

        }


    }
    public void deleteItemFromCart(Long id){

        if(!cartItemRepository.existsById(id)){
            throw new RuntimeException("Cart Item not found with id:" + id);
        }
        cartItemRepository.deleteById(id);

    }
    public void clearCart(Long userId){
        User user=userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found with this id: " + userId));
        cartItemRepository.deleteByUserId(userId);
    }

    public List<CartItem> getCartItems(Long userId){
        if(!userRepository.existsById(userId)){
            throw new RuntimeException("User  not found with id:" + userId);
        }
        return cartItemRepository.findByUserId(userId);
    }
    public CartItem updateQuantity(Long cartItemId, int quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart Item not found"));

        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }
}
