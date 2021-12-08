package com.example.shoppingmall.controller;

import com.example.shoppingmall.dto.OrderDto;
import com.example.shoppingmall.dto.OrderHistDto;
import com.example.shoppingmall.service.OrderService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(value = "/order")
    public @ResponseBody
        //비동기 처리를 위한 어노테이션 @ResponseBody @RequestBody
    ResponseEntity order(@RequestBody @Valid OrderDto orderDto
            , BindingResult bindingResult, Principal principal) {

        if (bindingResult.hasErrors()) {
            StringBuilder stringBuilder = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors) {
                stringBuilder.append(fieldError.getDefaultMessage());
            }
            return new ResponseEntity<String>(stringBuilder.toString(), HttpStatus.BAD_REQUEST);
        }
        String email = principal.getName(); // ID 정보를 가져온다.
        Long orderId;
        try {
            orderId = orderService.order(orderDto, email);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }

    @GetMapping(value = {"/order", "/order/{page}"})
    public String orderHist(@PathVariable("page") Optional<Integer> page, Principal principal, Model model) {
        Pageable pageable = PageRequest.of(page.orElse(0), 4);
        Page<OrderHistDto> orderHistDtoList = orderService.getOrderList(principal.getName(), pageable);
        model.addAttribute("orders", orderHistDtoList);
        model.addAttribute("page", pageable.getPageNumber());
        model.addAttribute("maxPage", 5);

        return "order/orderHist";
    }

    @PostMapping(value = "order/{orderId}/cancel")
    public @ResponseBody
    ResponseEntity cancelOrder(@PathVariable("orderId") Long orderId, Principal principal) {
        if (!orderService.validateOrder(orderId, principal.getName())) {
            return new ResponseEntity<String>("주문 취소 권한이 없습니다.", HttpStatus.FORBIDDEN);
        }
        orderService.cancelOrder(orderId);
        return new ResponseEntity<Long>(orderId, HttpStatus.OK);
    }


}
