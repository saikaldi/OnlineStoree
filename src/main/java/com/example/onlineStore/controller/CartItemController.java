package com.example.onlineStore.controller;

import com.example.onlineStore.model.CartItem;
import com.example.onlineStore.service.CartItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts")
public class CartItemController {
    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER', 'USER')")
    @PostMapping
    public ResponseEntity<CartItem> addCartItem(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        return ResponseEntity.ok(cartItemService.addItemToCart(userId, productId, quantity));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER', 'USER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long userId) {
        return ResponseEntity.ok(cartItemService.getCartItems(userId));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<CartItem> updateCart(@PathVariable Long id, @RequestParam int quantity){
        return ResponseEntity.ok(cartItemService.updateQuantity(id, quantity));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'SELLER', 'USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItemFromCart(@PathVariable Long id) {
        cartItemService.deleteItemFromCart(id);
        return ResponseEntity.ok("Item removed from cart");
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @DeleteMapping("/user/{userId}")
    public ResponseEntity<String> clearCart(@PathVariable Long userId) {
        cartItemService.clearCart(userId);
        return ResponseEntity.ok("Cart cleared for user: " + userId);
    }

}
