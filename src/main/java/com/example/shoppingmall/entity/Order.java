package com.example.shoppingmall.entity;

import com.example.shoppingmall.constant.OrderStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //주문 일자
    private LocalDateTime orderDate;

    //주문 상태
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "order", orphanRemoval = true,fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();


    private LocalDateTime regTime;

    private LocalDateTime updateTime;

}
