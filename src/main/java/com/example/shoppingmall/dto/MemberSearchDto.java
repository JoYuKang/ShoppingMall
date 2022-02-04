package com.example.shoppingmall.dto;

import com.example.shoppingmall.constant.Role;
import lombok.Data;

@Data
public class MemberSearchDto {
    //날짜별
    private String created_by;

    // 역할
    private String role;

    // 쿼리문
    private String searchQuery = "";

}
