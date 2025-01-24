package com.project.userorder.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.userorder.dto.OrderDTO;
import com.project.userorder.entity.Order;
import com.project.userorder.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;
    private Order order;
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        order = new Order(1L, "Product1", "Desc", 100.0, null);
    }

    @Test
    void createOrder_should_return_created_order() throws Exception {
        when(orderService.createOrder(any(Order.class))).thenReturn(order);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.productName").value(order.getProductName()))
                .andExpect(jsonPath("$.description").value(order.getDescription()))
                .andExpect(jsonPath("$.price").value(order.getPrice()));
    }

    @Test
    void getAllOrders_should_return_orders_list() throws Exception {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(order.getId());
        orderDTO.setProductName(order.getProductName());
        orderDTO.setDescription(order.getDescription());
        orderDTO.setPrice(order.getPrice());

        when(orderService.getAllOrders()).thenReturn(List.of(orderDTO));

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(order.getId()))
                .andExpect(jsonPath("$[0].productName").value(order.getProductName()));
    }

    @Test
    void getOrderById_should_return_order() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(java.util.Optional.of(order));

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(order.getId()))
                .andExpect(jsonPath("$.productName").value(order.getProductName()));
    }

    @Test
    void getOrderById_OrderNotFound_should_return_not_found() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isNotFound()); 
    }


    @Test
    void updateOrder_should_return_updated_order() throws Exception {
        when(orderService.updateOrder(1L, order)).thenReturn(order);

        mockMvc.perform(put("/orders/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productName").value(order.getProductName()))
                .andExpect(jsonPath("$.price").value(order.getPrice()));
    }

    @Test
    void deleteOrder_should_return_no_content() throws Exception {
        doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/orders/1"))
                .andExpect(status().isNoContent());
    }
}
