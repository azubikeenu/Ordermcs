package com.azubike.ellipsis.ordermcs.controller;

import com.azubike.ellipsis.ordermcs.dto.request.OrderRequestDto;
import com.azubike.ellipsis.ordermcs.dto.response.OrderResponseDto;
import com.azubike.ellipsis.ordermcs.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("orders")
public class OrderController {
  private final OrderService orderService;

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public Mono<OrderResponseDto> processOrder(
      @RequestBody Mono<OrderRequestDto> orderRequestDtoMono) {
    return orderService.processOrder(orderRequestDtoMono);
  }

  @GetMapping(value = "{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Flux<OrderResponseDto> getUserOrders(@PathVariable("userId") int userId) {
    return orderService.getOrdersById(userId);
  }
}
