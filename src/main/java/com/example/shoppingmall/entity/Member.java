package com.example.shoppingmall.entity;

import com.example.shoppingmall.constant.Role;
import com.example.shoppingmall.dto.MemberFormDto;
import com.example.shoppingmall.repository.MemberRepository;
import lombok.Data;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.MatchesPattern;
import javax.persistence.*;

@Data
@Table(name = "member")
@Entity
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.User);
        return member;
    }

    public static Member updateMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.User);
        return member;
    }

}
