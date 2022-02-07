package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.MemberFormDto;
import com.example.shoppingmall.entity.Member;
import com.example.shoppingmall.repository.MemberRepository;
import com.example.shoppingmall.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.stereotype.Service;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

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
    private MemberRepository memberRepository;

    @PersistenceContext
    EntityManager entityManager;

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

    @Test
    @DisplayName("auditing 테스트")
    @WithMockUser(username = "changgo",roles = "User")
    public void auditingTest(){
        Member member = new Member();

        memberRepository.save(member);

        entityManager.flush();
        entityManager.clear();

        Member member1 = memberRepository.findById(member.getId())
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("register time " + member1.getRegTime());
        System.out.println("update time " + member1.getUpdateTime());
        System.out.println("create user " + member1.getCreatedBy());
        System.out.println("modify user " + member1.getModifiedBy());



    }

    @Test
    @DisplayName("정보 수정 확인")
    @Transactional
    public void updateMember(){

        this.createMember("sdfsdf@sdfsdf","12341234");
        Member member = memberRepository.findByEmail("sdfsdf@sdfsdf");

        //수정 전
        System.out.println(memberRepository.findByEmail("sdfsdf@sdfsdf").getId());
        System.out.println(memberRepository.findByEmail("sdfsdf@sdfsdf").getName());


        member.setName("수정이 되는것인가");

        //수정 후
        System.out.println(memberRepository.findByEmail("sdfsdf@sdfsdf").getId());
        System.out.println(memberRepository.findByEmail("sdfsdf@sdfsdf").getName());

    }



}