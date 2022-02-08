package com.example.shoppingmall.repository;

import com.example.shoppingmall.entity.Board;
import com.example.shoppingmall.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

}
