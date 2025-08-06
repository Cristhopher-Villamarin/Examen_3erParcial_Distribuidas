package com.agroflow.cosecha_service.Listener;

import com.agroflow.cosecha_service.model.FacturaMessage;
import com.agroflow.cosecha_service.service.CosechaService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CosechaListener {
    private final CosechaService cosechaService;

    public CosechaListener(CosechaService cosechaService) {
        this.cosechaService = cosechaService;
    }

    @RabbitListener(queues = "cola_facturacion")
    public void handleFacturacionMessage(FacturaMessage message) {
        cosechaService.actualizarEstado(
                Long.parseLong(message.getCosechaId()), // <- conversión necesaria
                "FACTURADA",
                message.getFacturaId()
        );
    }

}