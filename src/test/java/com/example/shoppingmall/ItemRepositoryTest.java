package com.example.shoppingmall;

import com.example.shoppingmall.constant.ItemSellStatus;
import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.entity.QItem;
import com.example.shoppingmall.repository.ItemRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    ItemRepository itemRepository;

    @Test
    @DisplayName("item 저장 테스트")
    public void createItemTest() {
        Item item = new Item();

        item.setItemName("test item");
        item.setPrice(10000);
        item.setItemDetail("detail item test");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
        item.setRegTime(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        // item 저장
        Item savedItem = itemRepository.save(item);

        System.out.println(savedItem.toString());
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    void findByItemNmTest() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemName("테스트 상품1");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명")
    void findByItemNameOrItemDetail() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemNameOrItemDetail("테스트 상품1", "테스트 상품 1 상세");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격보다 낮은 아이템 ")
    void findByPriceLessThan() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByPriceLessThan(5000);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 오름차순 정렬")
    void findByPriceLessThanOrderByPrice() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPrice(5000);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Query로 findByIetmDetail")
    void findByItemDetail() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Query로 findByItemDetailByNative")
    void findByItemDetailByNative() {
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest() {
        this.createItemTest();
        JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        System.out.println("--------- query.fetch(); --------------");
        //query.fetch();
        List<Item> itemList = query.fetch();

//        System.out.println(itemList);
        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

    public void createItemList2() {
        for (int i = 0; i <= 5; i++) {
            Item item = new Item();
            item.setItemName("데이터 상품 " + i);
            item.setItemDetail("테스트 상품 상세 설명 " + i);
            item.setPrice(10000 * i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
            item.setUpdateTime(LocalDateTime.now());
            item.setRegTime(LocalDateTime.now());
            itemRepository.save(item);
        }
        for (int i = 6; i <= 10; i++) {

            Item item = new Item();
            item.setItemName("데이터 상품 " + i);
            item.setItemDetail("테스트 상품 상세 설명 " + i);
            item.setPrice(10000 * i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
            item.setUpdateTime(LocalDateTime.now());
            item.setRegTime(LocalDateTime.now());
            itemRepository.save(item);
        }


    }

    @Test
    void queryDslTest2() {
        this.createItemTest();
        createItemList2();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;
        String  itemDetail = "테스트 상품 상세 설명";
        int price = 30000;
        String itemSellStat = "SELL";
        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));

        if(StringUtils.equals(itemSellStat,ItemSellStatus.SELL)){
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        Pageable pageable = PageRequest.of(0,5);
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder,pageable);
        System.out.println("total elements : " + itemPagingResult.getTotalElements());

        List<Item> itemList = itemPagingResult.getContent();
        for(Item i : itemList){
            System.out.println(i.toString());
        }

    }
}