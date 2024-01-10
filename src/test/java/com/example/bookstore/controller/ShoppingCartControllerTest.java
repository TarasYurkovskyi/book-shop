package com.example.bookstore.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.bookstore.dto.cart.CartItemRequestDto;
import com.example.bookstore.dto.cart.CartItemResponseDto;
import com.example.bookstore.dto.cart.ShoppingCartDto;
import com.example.bookstore.model.Role;
import com.example.bookstore.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import javax.sql.DataSource;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
    private static CartItemRequestDto cartItemRequestDto;
    private static CartItemResponseDto book;
    private static ShoppingCartDto responseShoppingCartWithAddedItem;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(
            @Autowired DataSource dataSource,
            @Autowired WebApplicationContext applicationContext
    ) throws SQLException {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();

        teardown(dataSource);

        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/add-books.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/add-users.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/add-shopping-carts.sql")
            );
        }
        book = new CartItemResponseDto()
                .setId(1L)
                .setBookId(1L)
                .setBookTitle("Book1")
                .setQuantity(10);

        cartItemRequestDto = new CartItemRequestDto()
                .setBookId(1L)
                .setQuantity(10);

        responseShoppingCartWithAddedItem = new ShoppingCartDto()
                .setId(1L)
                .setUserId(1L)
                .setCartItems(List.of(book));

    }

    @AfterAll
    static void afterAll(
            @Autowired DataSource dataSource
    ) {
        teardown(dataSource);
    }

    @SneakyThrows
    static void teardown(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            connection.setAutoCommit(true);
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/delete-cart-items.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/delete-shopping-carts.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/delete-users.sql")
            );
            ScriptUtils.executeSqlScript(
                    connection,
                    new ClassPathResource("database/delete-books.sql")
            );
        }
    }

    @BeforeEach
    void setUp() {
        Role role = new Role()
                .setId(1L)
                .setName(Role.RoleName.USER);

        User user = new User()
                .setId(1L)
                .setEmail("user@gmail.com")
                .setPassword("password")
                .setRoles(Set.of(role));

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                user, user.getPassword(), user.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Add item to shopping cart")
    void addItem_ValidRequest_Ok() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/cart")
                                .content(objectMapper.writeValueAsString(cartItemRequestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        ShoppingCartDto expected = responseShoppingCartWithAddedItem;
        ShoppingCartDto actual = objectMapper.readValue(
                mvcResult.getResponse().getContentAsByteArray(),
                ShoppingCartDto.class
        );

        assertEquals(expected, actual);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    @DisplayName("Delete an item from shopping cart")
    void deleteItem_ValidId_Ok() throws Exception {
        Long itemId = 1L;

        mockMvc.perform(
                        delete("/api/cart/cart-items/{id}", itemId)
                )
                .andExpect(status().isNoContent());
    }
}
