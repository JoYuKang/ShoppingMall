package com.example.shoppingmall.entity;

import com.example.shoppingmall.constant.DeleteStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String Content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private DeleteStatus isDeleted;


    public static Board createBbs(Member member) {
        Board board = new Board();
        board.setMember(member);
        return board;
    }


}
