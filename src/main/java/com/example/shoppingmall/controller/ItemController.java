package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.ItemDto;
import com.example.shoppingmall.dto.ItemFormDto;
import com.example.shoppingmall.dto.ItemSearchDto;
import com.example.shoppingmall.entity.Item;
import com.example.shoppingmall.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.RequestFacade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.extras.springsecurity5.util.SpringVersionSpecificUtils;
import org.yaml.snakeyaml.events.CommentEvent;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping(value = "/admin/item/new")
    private String itemForm(Model model) {
        model.addAttribute("itemFormDto", new ItemFormDto());
        return "item/itemForm";
    }


    @PostMapping(value = "/admin/item/new")
    private String itemNew(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, Model model
            , @RequestParam("itemImgFile") List<MultipartFile> itemImgFileList) {
        if (bindingResult.hasErrors()) {
            return "item/itemForm";
        }
        if (itemImgFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errorMessage", "첫번째 상품 이미지를 넣어야합니다.");
            return "item/itemForm";
        }

        try {
            itemService.saveItem(itemFormDto, itemImgFileList);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "상품 등록 오류가 발생 ㅜㅜ");
            return "item/itemForm";
        }
        return "redirect:/";

    }

    @GetMapping(value = "/admin/item/{itemId}")
    private String itemDtl(@PathVariable("itemId") Long itemId, Model model) {
        try {
            ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
            model.addAttribute("itemFormDto", itemFormDto);
        } catch (Exception e) {
            model.addAttribute("errormessage", "존재하지 않는 상품입니다.");
            model.addAttribute("itemFormDto", new ItemFormDto());
            return "item/itemModForm";
        }

        return "item/itemModForm";
    }

    @GetMapping(value = "/item/{itemId}")
    private String itemDtl(Model model, @PathVariable("itemId") Long itemId) {
        ItemFormDto itemFormDto = itemService.getItemDtl(itemId);
        model.addAttribute("item", itemFormDto);
        return "item/itemDtl";
    }


    @PostMapping(value = "/admin/item/{itemId}")
    private String itemUpdate(@Valid ItemFormDto itemFormDto, BindingResult bindingResult, @RequestParam("itemImgFile")
            List<MultipartFile> itemFileList, Model model) {
        if (bindingResult.hasErrors()) {
            return "item/itemModForm";
        }
        if (itemFileList.get(0).isEmpty() && itemFormDto.getId() == null) {
            model.addAttribute("errormessage", "처음 이미지는 반드시 등록되어야 합니다.");
            return "item/itemModForm";
        }
        try {
            itemService.updateItem(itemFormDto, itemFileList);
        } catch (Exception e) {
            model.addAttribute("errormessage", "상품 수정 중 오류가 발생!");
            return "item/itemModForm";
        }
        return "redirect:/";

    }

    @GetMapping(value = {"/admin/items", "/admin/items/{page}"})
    public String itemManage(ItemSearchDto itemSearchDto, @PathVariable("page") Optional<Integer> page, Model model) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 3);
        Page<Item> items = itemService.getAdminItemPage(itemSearchDto, pageable);
        model.addAttribute("items", items);
        model.addAttribute("itemSearchDto", itemSearchDto);
        model.addAttribute("MaxPage", 5);
        return "item/itemManager";
    }
}
