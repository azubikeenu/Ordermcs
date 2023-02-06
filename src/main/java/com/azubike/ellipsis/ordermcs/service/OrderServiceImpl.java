package com.azubike.ellipsis.ordermcs.service;

import com.azubike.ellipsis.ordermcs.client.ProductClient;
import com.azubike.ellipsis.ordermcs.client.UserClient;
import com.azubike.ellipsis.ordermcs.dto.request.OrderRequestDto;
import com.azubike.ellipsis.ordermcs.dto.request.RequestContext;
import com.azubike.ellipsis.ordermcs.dto.response.OrderResponseDto;
import com.azubike.ellipsis.ordermcs.repository.OrderRepository;
import com.azubike.ellipsis.ordermcs.utils.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
  private final UserClient userClient;
  private final ProductClient productClient;
  private final OrderRepository repository;

  @Override
  @Transactional
  public Mono<OrderResponseDto> processOrder(Mono<OrderRequestDto> orderRequestDtoMono) {
    return orderRequestDtoMono
        .map(RequestContext::new)
        .flatMap(this::setProductDto)
        .flatMap(this::setUserDto)
        .doOnNext(ModelMapper::setTransactionRequestDto)
        .flatMap(this::performTransaction)
        .map(ModelMapper::toPurchaseOrder)
        .map(repository::save)
        .map(ModelMapper::toOrderResponse)
        .subscribeOn(Schedulers.boundedElastic());
  }

  @Override
  public Flux<OrderResponseDto> getOrdersById(int userId) {
    return Flux.fromStream(() -> repository.findByUserId(userId).stream())
        .map(ModelMapper::toOrderResponse)
        .subscribeOn(Schedulers.boundedElastic());
  }

  private Mono<RequestContext> setProductDto(RequestContext requestContext) {
    return productClient
        .getProductById(requestContext.getOrderRequestDto().getProductId())
        .doOnNext(requestContext::setProductDto)
        .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(1)))
        .thenReturn(requestContext);
  }

  private Mono<RequestContext> setUserDto(RequestContext requestContext) {
    return userClient
        .getUserById(requestContext.getOrderRequestDto().getUserId())
        .doOnNext(requestContext::setUserDto)
        .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(1)))
        .thenReturn(requestContext);
  }

  private Mono<RequestContext> performTransaction(RequestContext requestContext) {
    return userClient
        .makeTransaction(requestContext.getTransactionRequestDto())
        .doOnNext(requestContext::setTransactionResponseDto)
        .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(1)))
        .thenReturn(requestContext);
  }
}
