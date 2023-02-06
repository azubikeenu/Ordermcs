package com.azubike.ellipsis.ordermcs.dto.response;

import lombok.Data;

@Data
public class TransactionResponseDto {
    private Integer userId ;
    private TransactionStatus status;
    private Integer amount ;
}
