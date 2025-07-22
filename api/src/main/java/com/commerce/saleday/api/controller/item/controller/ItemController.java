package com.commerce.saleday.api.controller.item.controller;

import com.commerce.saleday.api.common.utils.PageUtils;
import com.commerce.saleday.api.controller.item.model.ItemResponseDto;
import com.commerce.saleday.api.controller.item.model.ItemReviewResponseDto;
import com.commerce.saleday.api.controller.item.model.ItemStockResponseDto;
import com.commerce.saleday.item.service.item.ItemService;
import com.commerce.saleday.order.service.stock.ItemStockService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ItemController {

  private final ItemService itemService;
  private final ItemStockService itemStockService;

  //Item 정보를 사용자에게 노출
  @GetMapping("/item/{itemCode}")
  public ItemResponseDto explainItem(@PathVariable String itemCode) {

    return ItemResponseDto.toResponse(itemService.getItem(itemCode));
  }

  //Item 수량을 노출
  @GetMapping("/item/{itemCode}/stock")
  public ItemStockResponseDto getItemStock(@PathVariable String itemCode) {

    return ItemStockResponseDto.toResponse(itemCode, itemStockService.getItemStock(itemCode));
  }

  //Item List를 사용자에게 노출(페이징 처리)
  @GetMapping("/items")
  public Page<ItemResponseDto> getItems(
      @RequestParam(required = false) String code,
      Pageable pageable
  ) {
    pageable = PageUtils.sanitizePageable(pageable, 100);
    return ItemResponseDto.toResponsePage(itemService.getItemsByCodeContaining(code, pageable));
  }

  //Item에 매핑된 리뷰의 정보를 읽음
  @GetMapping("/item/{itemCode}/reviews")
  public List<ItemReviewResponseDto> getItemReviews(@PathVariable String itemCode) {

    return ItemReviewResponseDto.toResponse(itemService.getItemReviews(itemCode));
  }

}
