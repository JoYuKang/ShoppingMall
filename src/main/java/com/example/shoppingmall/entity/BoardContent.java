package com.example.shoppingmall.entity;

import com.example.shoppingmall.constant.DeleteStatus;
import lombok.Data;

import javax.persistence.*;

@Data
public class BoardContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private DeleteStatus isDeleted;

    private int count;

    private String imgUrl;

}
