package com.commerce.saleday.application.service.item;

import com.commerce.saleday.domain.item.model.Item;
import com.commerce.saleday.domain.item.repository.ItemRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    public Item getItem(String itemCode){
        return itemRepository.findItemByCode(itemCode).orElseThrow(() -> new EntityNotFoundException("상품 정보가 없습니다."));
    }
}
