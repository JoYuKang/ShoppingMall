package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.ItemFormDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ItemController {
    @GetMapping(value = "/admin/item/new")
    private String itemForm(Model model){
        model.addAttribute("itemFormDto",new ItemFormDto());
        return "item/itemForm";
    }
}
