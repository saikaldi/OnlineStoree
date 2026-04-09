package com.example.onlineStore.controller;

import com.example.onlineStore.model.Order;
import com.example.onlineStore.model.User;
import com.example.onlineStore.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/orders")
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping("/checkout")
    public ResponseEntity<Order> checkout(Authentication authentication) {
        // Берем пользователя из контекста безопасности
        User currentUser = (User) authentication.getPrincipal();
        return ResponseEntity.ok(orderService.placeOrder(currentUser.getId()));
    }
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping
    public ResponseEntity<List<Order>> getMyOrderHistory(Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();


        List<Order> orders = orderService.getUserOrders(currentUser.getId());

        return ResponseEntity.ok(orders);
    }
}
