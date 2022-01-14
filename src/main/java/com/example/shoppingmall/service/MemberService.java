package com.example.shoppingmall.service;

import com.example.shoppingmall.entity.Member;
import com.example.shoppingmall.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public Member updateMember(Member member,Long id) {
        UpdateValidateDuplicateMember(member);
        member.setId(id);
        return memberRepository.save(member);
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

    private void UpdateValidateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if (findMember == null) {
            throw new IllegalStateException("데이터베이스 오류로 회원 수정이 불가합니다. 관리자에게 연락하세요.");
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
}
