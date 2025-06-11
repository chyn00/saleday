package com.commerce.saleday.item;

import com.commerce.saleday.common.jpa.config.QuerydslConfig;
import com.commerce.saleday.item.service.item.ItemService;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.commerce.saleday")
@EntityScan(basePackages = "com.commerce.saleday")
@ComponentScan(basePackages = "com.commerce.saleday")
@Import(QuerydslConfig.class)
public class TestItemApplication {

    @Bean
    public DataInitForItemTest dataInitForOrderTest(ItemService itemService) {
        return new DataInitForItemTest(itemService);
    }
}
