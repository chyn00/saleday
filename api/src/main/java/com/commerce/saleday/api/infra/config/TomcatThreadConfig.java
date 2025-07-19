package com.commerce.saleday.api.infra.config;

import org.apache.coyote.ProtocolHandler;
import org.apache.coyote.AbstractProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;

/**
 * [Tomcat Thread Configuration]
 * - CPU 코어 수 기반으로 maxThreads, minSpareThreads 동적 설정
 * - Spring Boot 3.x (Tomcat 10.x 이상) 호환 방식 사용
 * - 목적: 단일 인스턴스에서 최대 처리량 확보 (I/O 성능 튜닝)
 */
@Configuration
public class TomcatThreadConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    // CPU 계산 - 물리 + 효율 코어 포함한 가상 CPU 수
    private final int cpuCores = Runtime.getRuntime().availableProcessors(); // ex: 10

    // Tomcat 설정
    // [중~고부하] 동시 처리 가능한 최대 워커 수
    // TPS 1,000 이상 처리 위한 수준
    // CPU * 30~50 수준 추천 (I/O bound 기준)
    // 너무 높으면 context switching 비용 증가
    private final int maxThreads = cpuCores * 30;

    // 톰캣 부팅 시 생성할 워커 수 (웜업 용도)
    // 과도하면 메모리 낭비
    private final int minSpareThreads = cpuCores * 15;

    // maxThreads 초과 시 요청을 대기시킬 큐 크기
    // TPS 폭주 시 처리 지연 없이 수용 가능
    // 큐 초과 시 503 반환 → 적절히 버퍼 확보
    private final int acceptCount = 850;

    // 클라이언트 연결 요청 대기 시간 (ms)
    // 네트워크 지연 수용을 위한 평균적 타임아웃
    // 너무 길면 리소스 점유, 짧으면 timeout ↑
    private final int connectionTimeoutMs = 5000;//5초 내로 계산

    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        factory.addConnectorCustomizers(connector -> {
            ProtocolHandler handler = connector.getProtocolHandler();

            // Tomcat의 내부 HTTP Protocol Handler 설정
            if (handler instanceof AbstractProtocol<?> protocol) {
                protocol.setMaxThreads(maxThreads);                 // 최대 워커 수
                protocol.setMinSpareThreads(minSpareThreads);       // 최소 워커 수
                protocol.setAcceptCount(acceptCount);               // 요청 대기 큐
                protocol.setConnectionTimeout(connectionTimeoutMs); // 연결 타임아웃(ms)
            }
        });
    }
}
