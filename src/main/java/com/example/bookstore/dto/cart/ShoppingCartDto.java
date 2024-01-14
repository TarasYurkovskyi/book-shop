package com.example.bookstore.dto.cart;

import java.util.List;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private List<CartItemResponseDto> cartItems;
}
