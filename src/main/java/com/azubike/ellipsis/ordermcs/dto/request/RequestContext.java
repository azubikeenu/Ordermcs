package com.azubike.ellipsis.ordermcs.dto.request;

import com.azubike.ellipsis.ordermcs.dto.response.OrderResponseDto;
import com.azubike.ellipsis.ordermcs.dto.response.ProductDto;
import com.azubike.ellipsis.ordermcs.dto.response.TransactionResponseDto;
import com.azubike.ellipsis.ordermcs.dto.response.UserDto;
import lombok.Data;

@Data
public class RequestContext {
  private TransactionRequestDto transactionRequestDto;
  private TransactionResponseDto transactionResponseDto;
  private ProductDto productDto;
  private OrderRequestDto orderRequestDto;
  private OrderResponseDto orderResponseDto;
  private UserDto userDto;

  public RequestContext(OrderRequestDto orderRequestDto) {
    this.orderRequestDto = orderRequestDto;
  }
}
