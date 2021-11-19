package com.example.shoppingmall.dto;

import com.example.shoppingmall.constant.ItemSellStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
public class ItemDto {

    private Long id; //상품 코드

    private String itemName; // 상품명

    private int price; //가격

    private String itemDetail; //상품 상세 설명

    private LocalDateTime regTime; // 등록 시간

    private LocalDateTime updateTime; // 수정 시간
}
