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
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
        .flatMap(this::getProductPrice)
        .doOnNext(ModelMapper::setTransactionRequestDto)
        .flatMap(this::performTransaction)
        .map(ModelMapper::toPurchaseOrder)
        .map(repository::save)
        .map(ModelMapper::toOrderResponse)
        .subscribeOn(Schedulers.boundedElastic());
  }

  private Mono<RequestContext> getProductPrice(RequestContext requestContext) {
    return productClient
        .getProductById(requestContext.getProductDto().getId())
        .doOnNext(requestContext::setProductDto)
        .thenReturn(requestContext);
  }

  private Mono<RequestContext> performTransaction(RequestContext requestContext) {
    return userClient
        .makeTransaction(requestContext.getTransactionRequestDto())
        .doOnNext(requestContext::setTransactionResponseDto)
        .thenReturn(requestContext);
  }
}