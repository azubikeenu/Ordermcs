package com.azubike.ellipsis.ordermcs.dto.response;

import lombok.Data;

@Data
public class OrderResponseDto {
  private int userId;
  private String productId;
  private int orderId;
  private double amount;
  private OrderStatus orderStatus;
}
