package com.project.userorder.service;

import com.project.userorder.entity.Order;
import com.project.userorder.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void createOrder() {
        Order order = new Order(1L, "Product", "Description", 100.0, null);
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order createdOrder = orderService.createOrder(order);

        assertNotNull(createdOrder);
        assertEquals("Product", createdOrder.getProductName());
        assertEquals(100.0, createdOrder.getPrice());
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void getOrderById_OrderFound() {
        Order order = new Order(1L, "Product", "Description", 100.0, null);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> foundOrder = orderService.getOrderById(1L);

        assertTrue(foundOrder.isPresent());
        assertEquals(1L, foundOrder.get().getId());
    }

    @Test
    void getOrderById_OrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Order> foundOrder = orderService.getOrderById(1L);

        assertFalse(foundOrder.isPresent());
    }

    @Test
    void updateOrder() {
        Order existingOrder = new Order(1L, "Product", "Description", 100.0, null);
        Order updatedOrder = new Order(1L, "Updated Product", "Updated Description", 150.0, null);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(existingOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(updatedOrder);

        Order result = orderService.updateOrder(1L, updatedOrder);

        assertNotNull(result);
        assertEquals("Updated Product", result.getProductName());
        assertEquals(150.0, result.getPrice());
    }

//    @Test
//    void deleteOrder() {
//        Order order = new Order(1L, "Product", "Description", 100.0, null);
//
//        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
//        doNothing().when(orderRepository).deleteById(1L);
//
//        orderService.deleteOrder(1L);
//
//        verify(orderRepository, times(1)).deleteById(1L);
//    }
}
