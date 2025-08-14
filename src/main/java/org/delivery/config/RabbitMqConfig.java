package org.delivery.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "spring.rabbitmq.enabled", havingValue = "true", matchIfMissing = false)
public class RabbitMqConfig {
    // RabbitMQ 설정이 필요한 경우에만 활성화
    // 현재는 비활성화 상태
}
