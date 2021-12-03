package com.example.shoppingmall.repository;

import com.example.shoppingmall.dto.ItemSearchDto;
import com.example.shoppingmall.dto.MainItemDto;
import com.example.shoppingmall.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ItemRepositoryCustom {

    Page<Item> getAdminItemPage(ItemSearchDto itemSearchDto, Pageable pageable);

    Page<MainItemDto> getMainItemPage(ItemSearchDto itemSearchDto, Pageable pageable);
}
