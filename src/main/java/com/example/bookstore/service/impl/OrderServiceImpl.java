package com.example.bookstore.service.impl;

import com.example.bookstore.dto.order.CreateOrderItemDto;
import com.example.bookstore.dto.order.OrderResponseDto;
import com.example.bookstore.dto.order.UpdateOrderItemDto;
import com.example.bookstore.exception.EntityNotFoundException;
import com.example.bookstore.mapper.OrderMapper;
import com.example.bookstore.model.Order;
import com.example.bookstore.model.OrderItem;
import com.example.bookstore.model.ShoppingCart;
import com.example.bookstore.model.User;
import com.example.bookstore.repository.CartItemRepository;
import com.example.bookstore.repository.OrderItemRepository;
import com.example.bookstore.repository.OrderRepository;
import com.example.bookstore.repository.ShoppingCartRepository;
import com.example.bookstore.service.OrderService;
import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Override
    public List<OrderResponseDto> getOrdersHistory(Long userId, Pageable pageable) {
        List<Order> ordersHistory = orderRepository.findAllByUserId(userId, pageable);
        return ordersHistory.stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponseDto placeOrder(User user, CreateOrderItemDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(user.getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't get shopping cart with users id: "
                        + user.getId())
        );
        Order order = formOrder(shoppingCart, requestDto);
        Order savedOrder = orderRepository.save(order);

        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(cartItem -> new OrderItem(savedOrder, cartItem.getBook(),
                        cartItem.getQuantity(), cartItem.getBook().getPrice()))
                .collect(Collectors.toSet());

        order.setOrderItems(orderItems);
        cartItemRepository.deleteAll(shoppingCart.getCartItems());

        return orderMapper.toDto(savedOrder);
    }

    @Override
    public OrderResponseDto updateOrderStatus(Long id, UpdateOrderItemDto requestDto) {
        Order order = orderRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Cannot get order by id: " + id)
        );
        order.setStatus(requestDto.getStatus());

        return orderMapper.toDto(orderRepository.save(order));
    }

    private Order formOrder(ShoppingCart shoppingCart, CreateOrderItemDto requestDto) {
        BigDecimal totalPrice = shoppingCart.getCartItems().stream()
                .map(i -> i.getBook().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setShippingAddress(requestDto.getShippingAddress());
        order.setUser(shoppingCart.getUser());
        order.setStatus(Order.Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setTotal(totalPrice);
        return order;
    }
}
