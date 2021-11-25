package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.MemberFormDto;
import com.example.shoppingmall.entity.Member;
import com.example.shoppingmall.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.stereotype.Service;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberControllerTest {
    @Autowired
    private MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    public Member createMember(String email, String password) {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setPassword(password);
        memberFormDto.setName("스프링 부트");
        memberFormDto.setAddress("서울특별시 서초구");
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception {
        String email = "test@email.com";
        String password = "1234";
        this.createMember(email,password);

        mockMvc.perform(formLogin().userParameter("email").loginProcessingUrl("/member/login")
                .user(email).password(password)).andExpect(SecurityMockMvcResultMatchers.authenticated());
    }


    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception {
        String email = "test@email.com";
        String password = "1234";
        mockMvc.perform(formLogin().userParameter("email").loginProcessingUrl("/member/login")
                .user(email).password("password")).andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }

}