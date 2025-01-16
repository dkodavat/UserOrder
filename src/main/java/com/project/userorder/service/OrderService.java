package com.project.userorder.service;

import com.project.userorder.dto.OrderDTO;
import com.project.userorder.entity.Order;
import com.project.userorder.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    
    public Order createOrder(Order order) {
        return orderRepository.save(order);
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
            .map(order -> {
                // Creating DTO
                OrderDTO dto = new OrderDTO();
                dto.setId(order.getId());
                dto.setProductName(order.getProductName());
                dto.setDescription(order.getDescription());
                dto.setPrice(order.getPrice());
                
                if (order.getUser() != null) {
                    dto.setUserId(order.getUser().getId());
                    dto.setUserName(order.getUser().getName());
                    dto.setUserEmail(order.getUser().getEmail());
                }
                return dto;
            })
            .collect(Collectors.toList());
    }

    public Optional<Order> getOrderById(Long id) {
        return orderRepository.findById(id);
    }

   
    public Order updateOrder(Long id, Order orderDetails) {
        return orderRepository.findById(id).map(order -> {
            order.setProductName(orderDetails.getProductName());
            order.setDescription(orderDetails.getDescription());
            order.setPrice(orderDetails.getPrice());
            order.setUser(orderDetails.getUser());
            return orderRepository.save(order);
        }).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}
