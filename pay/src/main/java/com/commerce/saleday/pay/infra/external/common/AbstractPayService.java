package com.commerce.saleday.pay.infra.external.common;

import com.commerce.saleday.order.domain.order.model.Orders;
import com.commerce.saleday.order.service.order.OrderService;
import org.springframework.transaction.annotation.Transactional;

// 추상 클래스는 인스턴스를 직접 생성하지 않음.(템플릿 역할을 통해, 다른 클래스가 사용 하도록 설계되었기 때문)
public abstract class AbstractPayService {

  //OrderService 의존성 주입을 각 서비스에서 하도록 변경
  protected abstract OrderService getOrderService();

  private Orders getOrder(String orderCode) {
    return getOrderService().getOrder(orderCode);
  }

  // 템플릿 고정 메서드임을 명시하고, 오버라이딩을 막기 위해 final 작성
  public final String pay(String userId, String orderCode) {

    Orders order = this.getOrder(orderCode);
    String tid = doRequestToExternalPay(userId, order);

    //todo: payment db에 저장 로직 까지 필요
    return tid;
  }

  // kakaoPayService, naverPayService 등에서 오버라이딩하여 사용
  // 외부에서 오버라이딩되어 작성되기 때문에, 내부호출이 아니어서 트랜잭션에 묶임(PG의 트랜잭션은 별개)
  protected abstract String doRequestToExternalPay(String userId, Orders order);

}
