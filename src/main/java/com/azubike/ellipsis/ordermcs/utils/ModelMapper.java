package com.azubike.ellipsis.ordermcs.utils;

import com.azubike.ellipsis.ordermcs.dto.request.RequestContext;
import com.azubike.ellipsis.ordermcs.dto.request.TransactionRequestDto;
import com.azubike.ellipsis.ordermcs.dto.response.OrderResponseDto;
import com.azubike.ellipsis.ordermcs.dto.response.OrderStatus;
import com.azubike.ellipsis.ordermcs.dto.response.TransactionStatus;
import com.azubike.ellipsis.ordermcs.entity.PurchaseOrder;
import org.springframework.beans.BeanUtils;

public class ModelMapper {
  public static void setTransactionRequestDto(RequestContext requestContext) {
    TransactionRequestDto transactionRequestDto = new TransactionRequestDto();
    transactionRequestDto.setAmount(requestContext.getProductDto().getPrice().intValue());
    transactionRequestDto.setUserId(requestContext.getOrderRequestDto().getUserId());
    requestContext.setTransactionRequestDto(transactionRequestDto);
  }

  public static PurchaseOrder toPurchaseOrder(RequestContext requestContext) {
    PurchaseOrder purchaseOrder = new PurchaseOrder();
    purchaseOrder.setAmount(requestContext.getTransactionResponseDto().getAmount());
    purchaseOrder.setUserId(requestContext.getTransactionResponseDto().getUserId());
    purchaseOrder.setProductId(requestContext.getProductDto().getId());
    OrderStatus orderStatus =
        requestContext.getTransactionResponseDto().getStatus().equals(TransactionStatus.APPROVED)
            ? OrderStatus.COMPLETED
            : OrderStatus.FAILED;
    purchaseOrder.setOrderStatus(orderStatus);
    return purchaseOrder;
  }

  public static OrderResponseDto toOrderResponse(PurchaseOrder purchaseOrder) {
    final OrderResponseDto orderResponseDto = new OrderResponseDto();
    BeanUtils.copyProperties(purchaseOrder, orderResponseDto);
    orderResponseDto.setOrderId(purchaseOrder.getId());
    return orderResponseDto;
  }
}
