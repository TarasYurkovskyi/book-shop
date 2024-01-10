package com.example.bookstore.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.bookstore.dto.cart.CartItemResponseDto;
import com.example.bookstore.dto.cart.ShoppingCartDto;
import com.example.bookstore.dto.cart.UpdateCartItemDto;
import com.example.bookstore.mapper.ShoppingCartMapper;
import com.example.bookstore.model.Book;
import com.example.bookstore.model.CartItem;
import com.example.bookstore.model.Category;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.CartItemRepository;
import com.example.bookstore.repository.ShoppingCartRepository;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ShoppingCartServiceImplTest {
    private static User user;
    private static Book book;
    private static Category category;
    private static ShoppingCart shoppingCart;
    private static CartItem cartItem;
    private static CartItemResponseDto updatedCartItemDto;
    private static ShoppingCartDto shoppingCartDto;
    private static ShoppingCartDto updatedShoppingCart;
    private static UpdateCartItemDto updateCartItemDto;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;

    @BeforeAll
    static void beforeAll() {
        user = new User()
                .setId(1L)
                .setEmail("user@gmail.com")
                .setPassword("password")
                .setFirstName("User")
                .setLastName("User")
                .setShippingAddress("Userivka");

        category = new Category()
                .setId(4L)
                .setName("Romance")
                .setDescription("Romance");

        book = new Book()
                .setId(1L)
                .setTitle("Book1")
                .setAuthor("Author1")
                .setIsbn("123456789")
                .setPrice(BigDecimal.valueOf(1.25))
                .setDescription("description")
                .setCoverImage("cover image")
                .setCategories(Set.of(category));

        shoppingCart = new ShoppingCart()
                .setId(3L)
                .setUser(user)
                .setCartItems(new HashSet<>());

        cartItem = new CartItem()
                .setId(1L)
                .setShoppingCart(shoppingCart)
                .setBook(book)
                .setQuantity(10);

        updatedCartItemDto = new CartItemResponseDto()
                .setId(1L)
                .setBookId(1L)
                .setBookTitle("Book1")
                .setQuantity(5);

        shoppingCartDto = new ShoppingCartDto()
                .setId(1L)
                .setUserId(1L)
                .setCartItems(List.of());

        updatedShoppingCart = new ShoppingCartDto()
                .setId(1L)
                .setUserId(1L)
                .setCartItems(List.of(updatedCartItemDto));

        updateCartItemDto = new UpdateCartItemDto()
                .setQuantity(5);
    }

    @Test
    @DisplayName("Get shopping cart")
    void getShoppingCart_ValidEmail_ReturnShoppingCartDto() {

        Mockito.when(shoppingCartRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.ofNullable(shoppingCart));
        Mockito.when(shoppingCartMapper.toDto(shoppingCart)).thenReturn(shoppingCartDto);

        ShoppingCartDto expected = shoppingCartDto;
        ShoppingCartDto actual = shoppingCartService.getShoppingCart(user.getEmail());

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update item")
    void updateItem_ValidQuantity_Ok() {
        Long userId = 1L;
        Long itemId = 1L;

        Mockito.when(shoppingCartRepository.findByUserId(userId))
                .thenReturn(Optional.of(shoppingCart));
        Mockito.when(cartItemRepository.findById(itemId))
                .thenReturn(Optional.of(cartItem));
        Mockito.when(cartItemRepository.save(cartItem)).thenReturn(cartItem);
        Mockito.when(shoppingCartMapper.toDto(cartItem.getShoppingCart()))
                .thenReturn(updatedShoppingCart);

        ShoppingCartDto expected = updatedShoppingCart;
        ShoppingCartDto actual = shoppingCartService.updateItem(
                userId, itemId, updateCartItemDto
        );

        assertEquals(expected, actual);
    }
}
