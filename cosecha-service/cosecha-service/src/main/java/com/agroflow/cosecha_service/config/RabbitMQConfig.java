package com.agroflow.cosecha_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE = "cosechaExchange";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue colaNuevaCosecha() {
        return new Queue("cola_nueva_cosecha", true);
    }

    @Bean
    public Queue colaFacturacion() {
        return new Queue("cola_facturacion", true);
    }

    @Bean
    public Binding bindingCosecha(Queue colaNuevaCosecha, TopicExchange exchange) {
        return BindingBuilder.bind(colaNuevaCosecha).to(exchange).with("cosecha.nueva");
    }
    // Configuración del convertidor JSON
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();  // Usar Jackson para convertir a JSON
    }


    @Bean
    public Binding bindingFacturacion(Queue colaFacturacion, TopicExchange exchange) {
        return BindingBuilder.bind(colaFacturacion).to(exchange).with("inventario.ajustado");
    }
}
