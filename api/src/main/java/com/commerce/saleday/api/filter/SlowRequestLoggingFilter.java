package com.commerce.saleday.api.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class SlowRequestLoggingFilter implements Filter {

    private static final long SLOW_THRESHOLD_MS = 1000; // 슬로우 기준: 1초 이상

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        long startTime = System.currentTimeMillis();

        try {
            chain.doFilter(request, response); // 실제 요청 처리
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            if (duration >= SLOW_THRESHOLD_MS) {
                HttpServletRequest req = (HttpServletRequest) request;
                log.warn("⏱️ !!!!!SLOW REQUEST: [{} {}] took {}ms",
                        req.getMethod(), req.getRequestURI(), duration);
            }
        }
    }
}
