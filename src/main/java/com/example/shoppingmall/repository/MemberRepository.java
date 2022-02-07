package com.example.shoppingmall.repository;

import com.example.shoppingmall.dto.ItemSearchDto;
import com.example.shoppingmall.dto.MemberFormDto;
import com.example.shoppingmall.dto.MemberSearchDto;
import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.entity.Member;
import org.hibernate.query.NativeQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface MemberRepository extends JpaRepository<Member, Long> , QuerydslPredicateExecutor<Member>, MemberRepositoryCustom {
    Member findByEmail(String Email);
}
