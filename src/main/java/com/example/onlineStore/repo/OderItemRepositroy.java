package com.example.onlineStore.repo;

import com.example.onlineStore.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OderItemRepositroy extends JpaRepository<OrderItem, Long> {
}
