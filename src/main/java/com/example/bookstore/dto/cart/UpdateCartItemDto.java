package com.example.bookstore.dto.cart;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateCartItemDto {
    @Positive
    private int quantity;
}
