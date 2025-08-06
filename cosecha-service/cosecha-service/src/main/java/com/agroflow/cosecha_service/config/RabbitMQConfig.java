package com.agroflow.cosecha_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.core.Queue;


@Configuration
public class RabbitMQConfig {
    @Bean
    public Queue cosechaQueue() {
        return new Queue("cosechaQueue", true);
    }

    @Bean
    public Queue facturacionQueue() {
        return new Queue("facturacionQueue", true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("cosechaExchange");
    }

    @Bean
    public Binding bindingCosecha(Queue cosechaQueue, DirectExchange exchange) {
        return BindingBuilder.bind(cosechaQueue).to(exchange).with("cosechaQueue");
    }

    @Bean
    public Binding bindingFacturacion(Queue facturacionQueue, DirectExchange exchange) {
        return BindingBuilder.bind(facturacionQueue).to(exchange).with("facturacionQueue");
    }
}
