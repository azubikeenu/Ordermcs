package com.azubike.ellipsis.ordermcs.client;

import com.azubike.ellipsis.ordermcs.dto.request.TransactionRequestDto;
import com.azubike.ellipsis.ordermcs.dto.response.TransactionResponseDto;
import com.azubike.ellipsis.ordermcs.dto.response.UserDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserClient {
  private final WebClient webClient;

  public UserClient(@Value("${user.service.url}") String url) {
    this.webClient = WebClient.builder().baseUrl(url).build();
  }

  public Mono<UserDto> getUserById(int id) {
    return this.webClient.get().uri("{id}", id).retrieve().bodyToMono(UserDto.class);
  }

  public Mono<TransactionResponseDto> makeTransaction(TransactionRequestDto transactionRequestDto) {
    return this.webClient
        .post()
        .uri("transaction")
        .bodyValue(transactionRequestDto)
        .retrieve()
        .bodyToMono(TransactionResponseDto.class);
  }
}
