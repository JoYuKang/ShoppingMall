package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.ItemSearchDto;
import com.example.shoppingmall.dto.MemberFormDto;
import com.example.shoppingmall.dto.MemberSearchDto;
import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.entity.Member;
import com.example.shoppingmall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor // Autowired 어노테이션 없이 의존성 주입 가능
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    public Member updateMember(Member member) {
        return memberRepository.save(member);
    }

    public Member checkMember(String email) {

        return memberRepository.findByEmail(email);
    }


    public Member saveKakaoMember(Member member) {
        if (!validateDuplicateKakoMember(member)) {
            return memberRepository.findByEmail(member.getEmail());
        }

        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }




    private boolean validateDuplicateKakoMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember != null) {
            loadUserByUsername(member.getEmail());
            return false;
        }
        return true;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }

        return User.builder().username(member.getEmail()).password(member.getPassword())
                .roles(member.getRole().toString()).build();
    }

    //맴버 전체 조회
//    @Transactional(readOnly = true) //읽기전용으로 slave를 호출 DB 부하 감소
//    public Page<Member> getMemberAll(MemberFormDto memberFormDto, Pageable pageable) {
//        return memberRepository.getAdminMemberPage(memberFormDto, pageable);
//    }
}
