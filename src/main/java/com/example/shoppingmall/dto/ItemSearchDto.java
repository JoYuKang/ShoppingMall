package com.example.shoppingmall.dto;

import com.example.shoppingmall.constant.ItemSellStatus;
import lombok.Data;

@Data
public class ItemSearchDto {

    //날짜별
    private String searchDateType;

    // 판매 상태
    private ItemSellStatus itemSellStatus;

    // 글, 작성자
    private String searchBy;

    // 쿼리문
    private String searchQuery = "";

}
