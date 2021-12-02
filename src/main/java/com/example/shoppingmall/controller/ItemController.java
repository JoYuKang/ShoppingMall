package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.ItemDto;
import com.example.shoppingmall.dto.ItemFormDto;
import com.example.shoppingmall.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

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

}
