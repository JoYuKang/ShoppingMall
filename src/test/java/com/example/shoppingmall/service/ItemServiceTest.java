package com.example.shoppingmall.service;

import com.example.shoppingmall.constant.ItemSellStatus;
import com.example.shoppingmall.dto.ItemFormDto;
import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.entity.ItemImg;
import com.example.shoppingmall.repository.ItemImgRepository;
import com.example.shoppingmall.repository.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    List<MultipartFile> createMultipartFiles() throws Exception {
        List<MultipartFile> multipartFileList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            String path = "E:/shopping_Img/item/";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile mockMultipartFile = new MockMultipartFile(path, imageName, "image/jpg", new byte[]{1, 2, 3, 4});
            multipartFileList.add(mockMultipartFile);
        }
        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "admin")
    void saveItem() throws Exception {
        ItemFormDto itemFormDto = new ItemFormDto();
        itemFormDto.setItemName("상품 테스트");
        itemFormDto.setItemSellStatus(ItemSellStatus.SELL);
        itemFormDto.setItemDetail("테스트 상품 details");
        itemFormDto.setPrice(100000);
        itemFormDto.setStockNumber(100);

        List<MultipartFile> multipartFileList = createMultipartFiles();
        Long itemId = itemService.saveItem(itemFormDto, multipartFileList);

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);

        Item item = itemRepository.findById(itemId).orElseThrow(EntityExistsException::new);

        assertEquals(itemFormDto.getItemName(),item.getItemName());
        assertEquals(itemFormDto.getItemDetail(),item.getItemDetail());
        assertEquals(itemFormDto.getItemSellStatus(),item.getItemSellStatus());
        assertEquals(itemFormDto.getPrice(),item.getPrice());
        assertEquals(itemFormDto.getStockNumber(),item.getStockNumber());
        assertEquals(multipartFileList.get(0).getOriginalFilename(),itemImgList.get(0).getOriImgName());


    }


}