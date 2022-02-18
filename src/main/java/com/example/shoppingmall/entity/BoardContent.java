package com.example.shoppingmall.entity;

import com.example.shoppingmall.constant.DeleteStatus;
import lombok.Data;

import javax.persistence.*;

@Data
public class BoardContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 게시물 이름
    private String title;

    // 게시물 내용
    private String content;

    // 삭제 여부
    @Enumerated(EnumType.STRING)
    private DeleteStatus isDeleted;

    // 조회수
    private int count;

    // 이미지 주소
    private String imgUrl;

}
