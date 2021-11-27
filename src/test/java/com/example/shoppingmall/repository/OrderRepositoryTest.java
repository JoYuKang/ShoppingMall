package com.example.shoppingmall.repository;

import com.example.shoppingmall.constant.ItemSellStatus;
import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.entity.Member;
import com.example.shoppingmall.entity.Order;
import com.example.shoppingmall.entity.OrderItem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class OrderRepositoryTest {
    @Autowired
    OrderRepository orderRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @PersistenceContext
    EntityManager entityManager;



    public Item createItem() {
        Item item = new Item();
        item.setItemName("test item");
        item.setItemDetail("item 1221");
        item.setRegTime(LocalDateTime.now());
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setPrice(10000);
        item.setUpdateTime(LocalDateTime.now());
        item.setStockNumber(100);
        return item;
    }

    public Order createOrder() {

        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setOrder(order);
            orderItem.setOrderPrice(10000);
            orderItem.setUpdateTime(LocalDateTime.now());
            orderItem.setCount(10 * i + 1);
            order.getOrderItems().add(orderItem);
        }
        Member member = new Member();
        memberRepository.save(member);
        order.setMember(member);
        orderRepository.save(order);

        return order;
    }

    @Test
    @DisplayName("영속성 확인 테스트")
    public void cascadeTest() {
        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = this.createItem();
            itemRepository.save(item);
            OrderItem orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(100);
            orderItem.setOrder(order);
            orderItem.setOrderPrice(10000);
            orderItem.setRegTime(LocalDateTime.now());
            order.getOrderItems().add(orderItem);

        }

        orderRepository.saveAndFlush(order);
        entityManager.clear();

        Order saveOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(3, saveOrder.getOrderItems().size());


    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest() {
        Order order = createOrder();
        order.getOrderItems().remove(0);
        entityManager.flush();
    }

    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();
        entityManager.flush();
        entityManager.clear();


        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println("order class : " + orderItem.getOrder().getClass());
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        orderItem.getOrder().getOrderDate();
    }
}