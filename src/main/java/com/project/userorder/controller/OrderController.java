package com.project.userorder.controller;

import com.project.userorder.dto.OrderDTO;
import com.project.userorder.entity.Order;
import com.project.userorder.exception.ResourceNotFoundException;
import com.project.userorder.service.OrderService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    
    @PostMapping
    public ResponseEntity<?> createOrder(@Valid @RequestBody Order order) {
    	if(order.getProductName() == null || order.getProductName().isEmpty()) {
    		return ResponseEntity.badRequest().body(new ErrorResponse("name is required"));
    	}
    	if(order.getPrice()==null || order.getPrice()<=0) {
    		return ResponseEntity.badRequest().body(new ErrorResponse("price should be positive"));
    	}
    	 try {
             Order createdOrder = orderService.createOrder(order);
             return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
         } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("error while creating the order."));
         }
    }

    @GetMapping  
    public List<OrderDTO> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            Order order = orderService.getOrderById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("order not found"));
            return ResponseEntity.ok(order);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("order not found"));
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(@PathVariable Long id, @RequestBody Order orderDetails) {
        try {
        	Order updatedOrder= orderService.updateOrder(id, orderDetails);
            return ResponseEntity.ok(updatedOrder);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("order not found"));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
    	try {
            orderService.deleteOrder(id);
            return ResponseEntity.noContent().build();
    	}catch(ResourceNotFoundException e) {
    		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("order not found"));
    	}
    }
    public static class ErrorResponse{
    	private String error;
    	public ErrorResponse(String error) {
    		this.error= error;
    	}
    	public String getError() {
    		return error;
    	}
    }
    
}
