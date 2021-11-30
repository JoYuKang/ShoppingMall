package com.example.shoppingmall.service;

import com.example.shoppingmall.dto.ItemFormDto;
import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.entity.ItemImg;
import com.example.shoppingmall.repository.ItemImgRepository;
import com.example.shoppingmall.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    private final ItemImgService itemImgService;

    private final ItemImgRepository itemImgRepository;

    public Long saveItem(ItemFormDto itemFormDto, List<MultipartFile> itemIngFileList) throws Exception {
        // 상품 등록
        Item item = itemFormDto.createItem();
        itemRepository.save(item);

        //이미지 등록
        for (int i = 0; i < itemIngFileList.size(); i++) {
            ItemImg itemImg = new ItemImg();
            itemImg.setItem(item);
            if (i == 0) {
                itemImg.setRepimgYn("YES");
            } else {
                itemImg.setRepimgYn("NO");
            }
            itemImgService.saveItemImg(itemImg, itemIngFileList.get(i));
        }
        return item.getId();

    }


}
