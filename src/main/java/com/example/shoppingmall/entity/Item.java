package com.example.shoppingmall.entity;


import com.example.shoppingmall.constant.ItemSellStatus;
import com.example.shoppingmall.dto.ItemFormDto;
import com.example.shoppingmall.exception.OutOfStockException;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "item")
public class Item extends BaseEntity {

    @Id
    @Column(name = "item_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; //상품 코드

    @Column(nullable = false, length = 50)
    private String itemName; // 상품명

    @Column(name = "price", nullable = false)
    private int price; //가격

    @Column(nullable = false)
    private int stockNumber; //재고 수량

    @Lob
    @Column(nullable = false)
    private String itemDetail; //상품 상세 설명

    @Enumerated(EnumType.STRING)
    private ItemSellStatus itemSellStatus; //상품 판매 상태

    public void updateItem(ItemFormDto itemFormDto) {
        this.itemName = itemFormDto.getItemName();
        this.itemDetail = itemFormDto.getItemDetail();
        this.itemSellStatus = itemFormDto.getItemSellStatus();
        this.price = itemFormDto.getPrice();
        this.stockNumber = itemFormDto.getStockNumber();

    }

    //주문 성공
    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber;
        if (restStock < 0) {
            throw new OutOfStockException("상품 재고가 부족합니다. (현재 재고 수량: " + this.stockNumber + " )");
        }
        this.stockNumber = restStock;
    }

    //주문 취소
    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }
}
