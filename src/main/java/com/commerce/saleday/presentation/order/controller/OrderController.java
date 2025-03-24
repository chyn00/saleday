package com.commerce.saleday.presentation.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    //단건 주문
    @PostMapping("/order/item/{itemCode}")
    public String orderItem(@PathVariable String itemCode){
        return "요청하신 Item Code" + itemCode + "를 주문 성공했습니다.";
    }
}
