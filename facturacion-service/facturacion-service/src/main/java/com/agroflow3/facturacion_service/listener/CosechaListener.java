package com.agroflow3.facturacion_service.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.agroflow3.facturacion_service.dto.CosechaDTO;
import com.agroflow3.facturacion_service.service.FacturaService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CosechaListener {

    @Autowired
    private FacturaService service;

    @Autowired
    private ObjectMapper mapper;

    @RabbitListener(queues = "cola_facturacion")
    public void recibirCosecha(String mensaje) {
        try {
            CosechaDTO dto = mapper.readValue(mensaje, CosechaDTO.class);
            service.procesarCosecha(dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
