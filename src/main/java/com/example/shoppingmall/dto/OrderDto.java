package com.example.shoppingmall.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class OrderDto {

    @NotNull(message = "상품 아이디는 필수 값입니다.")
    private Long itemId;

    @Min(value = 1, message = "상품 최소 주문 수량은 1개 입니다.")
    @Max(value = 999, message = "상품 최대 주문 수량은 999개 입니다. 더 많은 주문을 원할 시 판매자에게 문의하세요.")
    private int count;

}
