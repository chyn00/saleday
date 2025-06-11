package com.commerce.saleday.discount;


import org.springframework.boot.autoconfigure.SpringBootApplication;

//discount는 sql이 필요없기 때문에, jpa와 관련된 설정 사용 X
@SpringBootApplication(
    exclude = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class,
        org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration.class
    }
)
public class TestDiscountApplication {

}
