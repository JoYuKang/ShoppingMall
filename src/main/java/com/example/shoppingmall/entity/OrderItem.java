package com.example.shoppingmall.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class OrderItem extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩을 위한 설정
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;

    private int count;

//    private LocalDateTime regTime;
//
//    private LocalDateTime updateTime;

}
