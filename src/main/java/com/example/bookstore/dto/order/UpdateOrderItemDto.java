package com.example.bookstore.dto.order;

import com.example.bookstore.model.Order;
import lombok.Data;

@Data
public class UpdateOrderItemDto {
    private Order.Status status;
}
