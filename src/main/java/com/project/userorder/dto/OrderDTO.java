package com.project.userorder.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class OrderDTO {
    private Long id;
    private String productName;
    private String description;
    private Double price;
    private Long userId;
    private String userName;
    private String userEmail;
}
