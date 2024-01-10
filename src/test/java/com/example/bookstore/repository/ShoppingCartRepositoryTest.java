package com.example.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.bookstore.model.Role;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ShoppingCartRepositoryTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("Find shopping cart by user id")
    @Sql(scripts = {
            "classpath:database/add-users.sql",
            "classpath:database/add-shopping-carts.sql",
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:database/delete-shopping-carts.sql",
            "classpath:database/delete-users.sql",
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findShoppingCartByUserId_ValidId_ReturnShoppingCart() {
        Role role = new Role()
                .setId(1L)
                .setName(Role.RoleName.USER);

        User user = new User()
                .setId(1L)
                .setEmail("user@gmail.com")
                .setPassword("password")
                .setFirstName("User")
                .setLastName("User")
                .setShippingAddress("Userivka")
                .setRoles(Set.of(role));

        ShoppingCart shoppingCart = new ShoppingCart()
                .setId(1L)
                .setUser(user);

        Optional<ShoppingCart> actual = shoppingCartRepository.findByUserId(user.getId());

        assertEquals(shoppingCart, actual.get());
    }
}
