package com.example.shoppingmall;

import com.example.shoppingmall.constant.ItemSellStatus;
import com.example.shoppingmall.entity.Item;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

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
        for(Item item: itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("상품명, 상품상세설명")
    void  findByItemNameOrItemDetail(){
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemNameOrItemDetail("테스트 상품1","테스트 상품 1 상세");
        for(Item item: itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격보다 낮은 아이템 ")
    void  findByPriceLessThan(){
        this.createItemTest();
        List<Item> itemList = itemRepository.findByPriceLessThan(5000);
        for(Item item: itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("가격 오름차순 정렬")
    void  findByPriceLessThanOrderByPrice(){
        this.createItemTest();
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPrice(5000);
        for(Item item: itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Query로 findByIetmDetail")
    void  findByItemDetail(){
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for(Item item: itemList){
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Query로 findByItemDetailByNative")
    void  findByItemDetailByNative(){
        this.createItemTest();
        List<Item> itemList = itemRepository.findByItemDetailByNative("테스트 상품 상세 설명");
        for(Item item: itemList){
            System.out.println(item.toString());
        }
    }
}