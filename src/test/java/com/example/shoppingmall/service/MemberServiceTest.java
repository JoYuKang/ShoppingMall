package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.MemberFormDto;
import com.example.shoppingmall.entity.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional //같은 테스트를 중복으로 해도 오류가 발생하지 않기 위해서 사용
@TestPropertySource(locations = "classpath:application-test.properties")
class MemberServiceTest {
    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setName("길떵냥떵");
        memberFormDto.setAddress("길거리");
        memberFormDto.setEmail("streetcat@naver.com");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }


    @Test
    @DisplayName("회원가입 테스트")
    public void saveMemberTest() {
        Member member = createMember();
        Member saveMember = memberService.saveMember(member);

        assertEquals(member.getAddress(), saveMember.getAddress());
        assertEquals(member.getEmail(), saveMember.getEmail());
        assertEquals(member.getName(), saveMember.getName());
        assertEquals(member.getPassword(), saveMember.getPassword());
        assertEquals(member.getRole(), saveMember.getRole());

        System.out.println(member.getName());
    }

    @Test
    @DisplayName("회원가입 중복 확인 테스트")
    public void saveDuplicateMemberTest() {
        Member member1 = new Member();
        Member member2 = new Member();

        memberService.saveMember(member1);

        Throwable e = assertThrows(IllegalStateException.class,() ->{
            memberService.saveMember(member2);
        });
        assertEquals("이미 가입된 회원입니다.",e.getMessage());



    }
}