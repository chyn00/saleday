package com.commerce.saleday.api.presentation.item.controller;

import com.commerce.saleday.api.application.service.item.ItemService;
import com.commerce.saleday.api.presentation.item.model.ItemResponseDto;
import com.commerce.saleday.api.presentation.item.model.ItemReviewResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;

  //Item 정보를 사용자에게 노출
  @GetMapping("/item")
  public ItemResponseDto explainItem(@RequestParam String code) {

    return ItemResponseDto.toResponse(itemService.getItem(code));
  }

  //Item에 매핑된 리뷰의 정보를 읽음
  @GetMapping("/item/{itemCode}/reviews")
  public List<ItemReviewResponseDto> getItemReviews(@PathVariable String itemCode) {

    return ItemReviewResponseDto.toResponse(itemService.getItemReviews(itemCode));
  }

}
