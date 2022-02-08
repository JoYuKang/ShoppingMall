package com.example.shoppingmall.dto;

import com.example.shoppingmall.constant.DeleteStatus;
import com.example.shoppingmall.entity.Member;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

class BoardDetailDto {

    private Long id; //게시글 번호

    private String title; // 제목

    private Integer content; //내용

    private String isDeleted; //삭제 여부

    private LocalDateTime regTime; // 등록 시간

    private LocalDateTime updateTime; // 수정 시간

}
