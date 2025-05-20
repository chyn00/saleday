package com.commerce.saleday.pay.infra.external.model;

import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class KakaoPayReadyRequest {

  private final String cid;
  private final String partner_order_id;
  private final String partner_user_id;
  private final String item_name;
  private final String quantity;
  private final String total_amount;
  private final String vat_amount;
  private final String tax_free_amount;
  private final String approval_url;
  private final String fail_url;
  private final String cancel_url;

  public Map<String, Object> toBody() {
    return Map.ofEntries(
        Map.entry("cid", cid),
        Map.entry("partner_order_id", partner_order_id),
        Map.entry("partner_user_id", partner_user_id),
        Map.entry("item_name", item_name),
        Map.entry("quantity", quantity),
        Map.entry("total_amount", total_amount),
        Map.entry("vat_amount", vat_amount),
        Map.entry("tax_free_amount", tax_free_amount),
        Map.entry("approval_url", approval_url),
        Map.entry("fail_url", fail_url),
        Map.entry("cancel_url", cancel_url)
    );
  }
}
