package com.project.userorder.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.project.userorder.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
