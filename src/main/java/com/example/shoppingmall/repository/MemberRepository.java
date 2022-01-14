package com.example.shoppingmall.repository;

import com.example.shoppingmall.entity.Member;
import org.hibernate.query.NativeQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(String Email);

}
