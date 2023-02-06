package com.azubike.ellipsis.ordermcs;

import com.azubike.ellipsis.ordermcs.client.ProductClient;
import com.azubike.ellipsis.ordermcs.client.UserClient;
import com.azubike.ellipsis.ordermcs.dto.request.OrderRequestDto;
import com.azubike.ellipsis.ordermcs.dto.response.OrderResponseDto;
import com.azubike.ellipsis.ordermcs.dto.response.ProductDto;
import com.azubike.ellipsis.ordermcs.dto.response.UserDto;
import com.azubike.ellipsis.ordermcs.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest
class OrdermcsApplicationTests {

  @Autowired ProductClient productClient;
  @Autowired UserClient userClient;

  @Autowired OrderService orderService;

  @Test
  void contextLoads() {
    final Flux<OrderResponseDto> orderResponseDtoFlux =
        Flux.zip(productClient.getAllProducts(), userClient.getAllUsers())
            .map(t -> buildOrderRequest(t.getT1(), t.getT2()))
            .flatMap(orderService::processOrder)
            .log();

    StepVerifier.create(orderResponseDtoFlux)
        .expectSubscription()
        .expectNextCount(4)
        .verifyComplete();
  }

  Mono<OrderRequestDto> buildOrderRequest(ProductDto productDto, UserDto userDto) {
    final OrderRequestDto orderRequestDto = new OrderRequestDto();
    orderRequestDto.setProductId(productDto.getId());
    orderRequestDto.setUserId(userDto.getId());
    return Mono.just(orderRequestDto);
  }
}
