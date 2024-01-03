package com.example.bookstore.service.impl;

import com.example.bookstore.dto.cart.CartItemRequestDto;
import com.example.bookstore.dto.cart.ShoppingCartDto;
import com.example.bookstore.dto.cart.UpdateCartItemDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.CartItemMapper;
import com.example.bookstore.mapper.ShoppingCartMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.repository.BookRepository;
import com.example.bookstore.repository.CartItemRepository;
import com.example.bookstore.repository.ShoppingCartRepository;
import com.example.bookstore.repository.UserRepository;
import com.example.bookstore.service.ShoppingCartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartDto getShoppingCart(String email) {
        return shoppingCartMapper.toDto(shoppingCartRepository.findByEmail(email).orElseThrow(
                () -> new EntityNotFoundException("Cannot get shopping cart by email: " + email)
        ));
    }

    @Override
    @Transactional
    public ShoppingCartDto addItem(Long id, CartItemRequestDto requestDto) {
        CartItem cartItem = cartItemMapper.toEntity(requestDto);
        Book bookById = bookRepository.findById(requestDto.getBookId()).orElseThrow(
                () -> new EntityNotFoundException("Cannot get book by id: " + id)
        );

        cartItem.setBook(bookById);

        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(id).orElseGet(() -> {
            ShoppingCart newShoppingCart = new ShoppingCart();
            newShoppingCart.setUser(userRepository.findById(id).orElseThrow(
                    () -> new EntityNotFoundException("Cannot find user by ID: " + id)
            ));
            shoppingCartRepository.save(newShoppingCart);
            return newShoppingCart;
        });
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setQuantity(requestDto.getQuantity());
        CartItem savedItem = cartItemRepository.save(cartItem);
        shoppingCart.getCartItems().add(savedItem);

        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto updateItem(
            Long userId,
            Long itemId,
            UpdateCartItemDto requestDto
    ) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId).orElseThrow(
                () -> new EntityNotFoundException("Cannot get shopping cart by id: "
                        + userId)
        );

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(
                        () -> new EntityNotFoundException("Cannot get item by id: " + itemId)
                );

        cartItem.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(cartItem);

        return shoppingCartMapper.toDto(cartItem.getShoppingCart());
    }

    @Override
    public void deleteCartItemById(Long userId, Long id) {
        cartItemRepository.deleteById(id);
    }
}
