package com.commerce.saleday.pay.infra.external.kakaopay.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoPayReadyResponse {

    public static final String KAKAO_PAY_RESPONSE = "kakao-pay-response:";

    private String tid;

    @JsonProperty("tms_result")
    private boolean tmsResult;

    @JsonProperty("created_at")
    private String createdAt;

    @JsonProperty("next_redirect_pc_url")
    private String nextRedirectPcUrl;

    @JsonProperty("next_redirect_mobile_url")
    private String nextRedirectMobileUrl;

    @JsonProperty("next_redirect_app_url")
    private String nextRedirectAppUrl;

    @JsonProperty("android_app_scheme")
    private String androidAppScheme;

    @JsonProperty("ios_app_scheme")
    private String iosAppScheme;

    public static String getRedisKey(String userId) {
        return KAKAO_PAY_RESPONSE + userId;
    }
}
