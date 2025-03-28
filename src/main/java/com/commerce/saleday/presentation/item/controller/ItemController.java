package com.commerce.saleday.presentation.item.controller;

import com.commerce.saleday.application.service.item.ItemService;
import com.commerce.saleday.presentation.item.model.ItemReviewResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    //Item 정보를 사용자에게 노출
    @GetMapping("/item")
    public String explainItem(@RequestParam String code){

        return "요청하신 Item Code : " + code + "의 상품명은 ("+ itemService.getItem(code).getName() + ") 입니다.";
    }

    //Item에 매핑된 리뷰의 정보를 읽음
    @GetMapping("/item/{itemCode}/reviews")
    public List<ItemReviewResponseDto> getItemReviews(@PathVariable String itemCode){

        return ItemReviewResponseDto.toResponse(itemService.getItemReviews(itemCode));
    }

}
