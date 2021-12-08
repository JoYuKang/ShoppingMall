package com.example.shoppingmall.dto;

import com.example.shoppingmall.entity.OrderItem;
import lombok.Data;

@Data
public class OrderItemDto {


    private String itemName;

    private int count;

    private int orderPrice;

    private String imgUrl;

    private long itemId;

    public OrderItemDto(OrderItem orderItem, String imgUrl) {
        this.itemName = orderItem.getItem().getItemName();
        this.count = orderItem.getCount();
        this.imgUrl = imgUrl;
        this.orderPrice = orderItem.getOrderPrice();
        this.itemId = orderItem.getItem().getId();
    }

}
