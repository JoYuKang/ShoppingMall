package com.example.shoppingmall.repository;

import com.example.shoppingmall.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String Email);
}
