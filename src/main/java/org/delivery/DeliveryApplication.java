package org.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"org.delivery", "org.delivery.api", "org.delivery.storeadmin"})
@EntityScan(basePackages = "org.delivery.db")
@EnableJpaRepositories(basePackages = "org.delivery.db")
public class DeliveryApplication {
    public static void main(String[] args) {
        SpringApplication.run(DeliveryApplication.class, args);
    }
}
