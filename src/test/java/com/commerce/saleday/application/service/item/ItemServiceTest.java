package com.commerce.saleday.application.service.item;

import com.commerce.saleday.domain.item.model.Item;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Test
    void getItem() {
        //given
        String itemCode = "1234";

        //when
        Item item = itemService.getItem(itemCode);

        //then
        assertThat(item).isNotNull();
    }
}