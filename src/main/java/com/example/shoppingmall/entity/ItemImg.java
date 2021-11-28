package com.example.shoppingmall.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "item_img")
public class ItemImg extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imgName; // 이미지 파일명

    private String oriImgName; //원본 이미지 파일명

    private String imgUrl; // 이미지 조회 경로

    private String repimgYn;  //대표 이미지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    public void updateItemImg(String oriImgName, String imgUrl, String imgName) {
        this.oriImgName = oriImgName;
        this.imgName = imgName;
        this.imgUrl = imgUrl;
    }

}
