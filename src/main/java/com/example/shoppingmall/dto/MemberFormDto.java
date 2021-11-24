package com.example.shoppingmall.dto;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Data;

import javax.annotation.MatchesPattern;

@Data
public class MemberFormDto {

    private String name;

    @NotNull
    private String email;

    private String password;

    private String address;

}
