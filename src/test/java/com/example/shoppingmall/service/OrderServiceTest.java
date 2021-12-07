package com.example.shoppingmall.service;

import com.example.shoppingmall.constant.ItemSellStatus;
import com.example.shoppingmall.constant.Role;
import com.example.shoppingmall.dto.OrderDto;
import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.entity.Member;
import com.example.shoppingmall.entity.Order;
import com.example.shoppingmall.entity.OrderItem;
import com.example.shoppingmall.repository.ItemRepository;
import com.example.shoppingmall.repository.MemberRepository;
import com.example.shoppingmall.repository.OrderRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    MemberRepository memberRepository;

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
    @DisplayName("주문 확인 테스트")
    void order() {
        Item item = saveItem();
        Member member = saveMember();

        OrderDto orderDto = new OrderDto();
        orderDto.setCount(10);
        orderDto.setItemId(item.getId());

        Long orderId = orderService.order(orderDto, member.getEmail());

        Order order = orderRepository.findById(orderId).orElseThrow(EntityNotFoundException::new);

        List<OrderItem> orderItems = order.getOrderItems();

        int totalPrice = orderDto.getCount() * item.getPrice();

        assertEquals(totalPrice, order.getTotalPrice());


    }
}