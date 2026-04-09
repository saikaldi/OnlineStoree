package com.example.onlineStore.service;

import com.example.onlineStore.model.*;
import com.example.onlineStore.repo.CartItemRepository;
import com.example.onlineStore.repo.OrderRepository;
import com.example.onlineStore.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    // Constructor Injection (Spring automatically injects these beans)
    public OrderService(OrderRepository orderRepository,
                        UserRepository userRepository,
                        CartItemRepository cartItemRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartItemRepository = cartItemRepository;
    }

    /**
     * Converts a User's shopping cart into a permanent Order.
     * Uses @Transactional to ensure that if any step fails, the database rolls back
     * (e.g., the cart won't be cleared if the order fails to save).
     */
    @Transactional
    public Order placeOrder(Long userId) {
        // 1. Fetch the user from the database
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Fetch all items currently in the user's cart
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);

        // 3. Validation: Prevent creating empty orders
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cannot place order: Cart is empty");
        }

        // 4. Initialize the Order "Header"
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.PENDING);

        // 5. Initialize total calculation using BigDecimal for financial precision
        BigDecimal total = BigDecimal.ZERO;

        // 6. Transform each CartItem into a permanent OrderItem
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());

            // CRITICAL: Snapshot the current price so history doesn't change
            // even if the product price is updated later in the store.
            BigDecimal currentPrice = cartItem.getProduct().getPrice();
            orderItem.setPriceAtPurchase(currentPrice.doubleValue());

            // Establish the bidirectional relationship
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);

            // Calculate sub-total for this line: (Price * Quantity)
            BigDecimal itemTotal = currentPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

            // Add to grand total: total = total + itemTotal
            total = total.add(itemTotal);
        }

        // 7. Set the final calculated amount
        order.setTotalAmount(total.doubleValue());

        // 8. Save the Order.
        // Because of CascadeType.ALL, all OrderItems are saved automatically.
        Order savedOrder = orderRepository.save(order);

        // 9. Clean up: Remove items from the cart now that they are ordered
        cartItemRepository.deleteAll(cartItems);

        return savedOrder;

    }

    public List<Order> getUserOrders(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}