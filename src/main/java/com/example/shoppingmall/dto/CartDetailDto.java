package com.example.shoppingmall.dto;

import lombok.Data;

@Data
public class CartDetailDto {

    private Long cartItemId;

    private String itemName;

    private int price;

    private int count;

    private String imgUrl;

    //private Long itemId;

    public CartDetailDto(Long cartItemId, String itemName, int price, int count
            , String imgUrl) {
        this.cartItemId = cartItemId;
        this.itemName = itemName;
        this.price = price;
        this.imgUrl = imgUrl;
        this.count = count;
       // this.itemId = itemId;
    }


}
