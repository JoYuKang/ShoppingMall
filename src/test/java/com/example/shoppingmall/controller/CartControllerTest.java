package com.example.shoppingmall.controller;

import com.example.shoppingmall.constant.ItemSellStatus;
import com.example.shoppingmall.constant.Role;
import com.example.shoppingmall.dto.CartItemDto;
import com.example.shoppingmall.entity.CartItem;
import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.entity.Member;
import com.example.shoppingmall.repository.CartItemRepository;
import com.example.shoppingmall.repository.ItemRepository;
import com.example.shoppingmall.repository.MemberRepository;
import com.example.shoppingmall.service.CartService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class CartControllerTest {

    @Autowired
    CartService cartService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    CartItemRepository cartItemRepository;

    public Item saveItem() {
        Item item = new Item();
        item.setItemName("테스트 상품 이름");
        item.setItemDetail("테스트 상품 상세");
        item.setStockNumber(100);
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setPrice(30000);
        return itemRepository.save(item);
    }

    public Member saveMember() {
        Member member = new Member();
        member.setName("시작");
        member.setPassword("1234");
        member.setAddress("서울특별시 서초구");
        member.setRole(Role.ADMIN);
        member.setEmail("asd@asd.com");

        return memberRepository.save(member);
    }

    @Test
    @DisplayName("장바구니")
    void addCart() {
        Item item = saveItem();
        Member member = saveMember();

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setItemId(item.getId());
        cartItemDto.setCount(5);

        Long cartItemId = cartService.addCart(cartItemDto, member.getEmail());

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(EntityNotFoundException::new);

        assertEquals(item.getId(), cartItem.getItem().getId());
        assertEquals(cartItemDto.getCount(), cartItem.getCount());

    }
}