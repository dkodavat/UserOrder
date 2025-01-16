package com.project.userorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.project.userorder.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}

