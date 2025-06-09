package com.commerce.saleday.pay;
import com.commerce.saleday.common.jpa.config.QuerydslConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
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
public class TestPayApplication {

}
