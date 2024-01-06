package com.example.bookstore.service;

import com.example.bookstore.dto.order.CreateOrderItemDto;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.dto.order.UpdateOrderItemDto;
import com.example.bookstore.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    List<OrderResponseDto> getOrdersHistory(Long userId, Pageable pageable);

    OrderResponseDto placeOrder(User user, CreateOrderItemDto requestDto);

    OrderResponseDto updateOrderStatus(Long id, UpdateOrderItemDto requestDto);
}
