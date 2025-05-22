package com.commerce.saleday.api.common.utils;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

//api page 리턴 util
public class PageUtils {

  //sanitize: 정상범위로 불필요한 데이터 정제
  public static Pageable sanitizePageable(Pageable pageable, int maxSize) {
    int page = Math.max(pageable.getPageNumber(), 0);
    int size = Math.min(pageable.getPageSize(), maxSize);
    return PageRequest.of(page, size, pageable.getSort());
  }

}
