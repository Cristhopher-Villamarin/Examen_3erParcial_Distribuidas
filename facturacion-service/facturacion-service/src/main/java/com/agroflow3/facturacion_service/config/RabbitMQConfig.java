package com.agroflow3.facturacion_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue colaFacturacion() {
        return QueueBuilder.durable("cola_facturacion").build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
