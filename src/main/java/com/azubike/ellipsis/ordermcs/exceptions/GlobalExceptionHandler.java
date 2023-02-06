package com.azubike.ellipsis.ordermcs.exceptions;

import io.netty.util.internal.StringUtil;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Optional;

@Order(-99)
@Component
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {
  public GlobalExceptionHandler(
      ErrorAttributes errorAttributes,
      WebProperties webProperties,
      ApplicationContext applicationContext,
      ServerCodecConfigurer configurer) {
    super(errorAttributes, webProperties.getResources(), applicationContext);
    this.setMessageReaders(configurer.getReaders());
    this.setMessageWriters(configurer.getWriters());
  }

  @Override
  protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
    return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
  }

  private Mono<ServerResponse> renderErrorResponse(ServerRequest request) {
    final String query = request.uri().getQuery();
    final ErrorAttributeOptions errorAttributeOptions =
        isStackTraceEnabled(query)
            ? ErrorAttributeOptions.of(ErrorAttributeOptions.Include.STACK_TRACE)
            : ErrorAttributeOptions.of(ErrorAttributeOptions.Include.MESSAGE);

    Map<String, Object> errorPropertiesMap = getErrorAttributes(request, errorAttributeOptions);
    final int status =
        (int)
            Optional.ofNullable(errorPropertiesMap.get("status"))
                .orElse(HttpStatus.INTERNAL_SERVER_ERROR);

    return ServerResponse.status(status)
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(errorPropertiesMap));
  }

  private boolean isStackTraceEnabled(String query) {
    return !StringUtil.isNullOrEmpty(query) && query.contains("trace=true");
  }
}