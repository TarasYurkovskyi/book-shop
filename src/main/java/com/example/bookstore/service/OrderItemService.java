package com.example.bookstore.service;

import com.example.bookstore.dto.order.OrderItemResponseDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface OrderItemService {
    List<OrderItemResponseDto> getAllOrderItemsFromOrder(Long orderId, Pageable pageable);

    OrderItemResponseDto getOrderItemFromOrder(Long userId, Long orderId, Long orderItemId);
}
