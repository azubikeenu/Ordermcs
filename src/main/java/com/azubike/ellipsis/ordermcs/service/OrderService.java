package com.azubike.ellipsis.ordermcs.service;

import com.azubike.ellipsis.ordermcs.dto.request.OrderRequestDto;
import com.azubike.ellipsis.ordermcs.dto.response.OrderResponseDto;
import reactor.core.publisher.Mono;

public interface OrderService {
  Mono<OrderResponseDto> processOrder(Mono<OrderRequestDto> orderRequestDtoMono);
}
