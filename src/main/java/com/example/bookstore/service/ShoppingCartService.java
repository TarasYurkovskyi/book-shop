package com.example.bookstore.service;

import com.example.bookstore.dto.cart.CartItemRequestDto;
import com.example.bookstore.dto.cart.ShoppingCartDto;
import com.example.bookstore.dto.cart.UpdateCartItemDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCart(String email);

    ShoppingCartDto addItem(Long id, CartItemRequestDto requestDto);

    ShoppingCartDto updateItem(Long userId, Long itemId,
                               UpdateCartItemDto requestDto);

    void deleteCartItemById(Long userId, Long id);
}
