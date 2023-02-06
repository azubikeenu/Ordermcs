package com.azubike.ellipsis.ordermcs.entity;

import com.azubike.ellipsis.ordermcs.dto.response.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
public class PurchaseOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private Integer userId;
  private Integer productId;
  private double amount;
  private OrderStatus orderStatus;
}
