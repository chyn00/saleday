package com.commerce.saleday.presentation.item.controller;

import com.commerce.saleday.application.service.item.ItemService;
import com.commerce.saleday.domain.item.model.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    //item 정보를 사용자에게 노출
    @GetMapping("/item")
    public String explainItem(@RequestParam String code){

        return "요청하신 Item Code : " + code + "의 상품명은 ("+ itemService.getItem(code).getName() + ") 입니다.";
    }

}
