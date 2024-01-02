package com.example.bookstore.repository;

import com.example.bookstore.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart,Long> {
    @Query("SELECT sc FROM ShoppingCart sc JOIN FETCH sc.cartItems")
    ShoppingCart findByEmail(String email);

    Optional<ShoppingCart> findByUserId(Long userId);
}
