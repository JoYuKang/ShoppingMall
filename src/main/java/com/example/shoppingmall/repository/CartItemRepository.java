package com.example.shoppingmall.repository;

import com.example.shoppingmall.dto.CartDetailDto;
import com.example.shoppingmall.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndItemId(Long cartId, Long itemId);

    @Query("select new com.example.shoppingmall.dto.CartDetailDto(ci.id, i.itemName, i.price, ci.count, im.imgUrl) "
            + "from CartItem ci, ItemImg im "
            + "join ci.item i "
            + "where ci.cart.id = :cartId "
            + "and im.item.id = ci.item.id "
            + "and im.repimgYn = 'YES' "
            + "order by ci.id desc "
    )
    List<CartDetailDto> findCartDetailDtoList(Long cartId);
}
