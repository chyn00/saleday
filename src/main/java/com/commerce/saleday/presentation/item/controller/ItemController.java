package com.commerce.saleday.presentation.item.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ItemController {

    //item 정보를 사용자에게 노출
    @GetMapping("/item")
    public String explainItem(@RequestParam String itemCode){
        return "요청하신 아이템 코드의 정보는 아래와 같습니다. (상품명 : , 가격 : , 상품코드 : " + itemCode+")";
    }

}
