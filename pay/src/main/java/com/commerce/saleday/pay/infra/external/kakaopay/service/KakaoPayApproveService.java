package com.commerce.saleday.pay.infra.external.kakaopay.service;

import com.commerce.saleday.common.exception.ExceptionCode;
import com.commerce.saleday.common.exception.SaleDayException;
import com.commerce.saleday.pay.common.utils.JwtUtilsStub;
import com.commerce.saleday.pay.infra.external.kakaopay.KakaoPayClient;
import com.commerce.saleday.pay.infra.external.kakaopay.model.KakaoPayForApproval;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KakaoPayApproveService {

  private final KakaoPayClient kakaoPayClient;
  private final RedisTemplate<String, Object> redisTemplate;

  //외부 API요청
  public String singlePayApproveRequest(String pgToken) {

    //todo: 실제의 경우 jwtUtils가 아닌 어노테이션을 활용하여, userId를 추출
    String userId = String.valueOf(JwtUtilsStub.getUserId());

    KakaoPayForApproval kakaoPayForApproval = (KakaoPayForApproval) redisTemplate.opsForValue()
        .get(KakaoPayForApproval.getRedisKey(userId));

    if (kakaoPayForApproval == null) {
      throw new SaleDayException(ExceptionCode.NO_SUCH_DATA);
    }

    return kakaoPayClient.requestApprove(kakaoPayForApproval.toKakaoPayApproveRequest(pgToken));
  }
}
