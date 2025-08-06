package com.agroflow2.inventario_service.controller;

import com.agroflow2.inventario_service.config.RabbitMQConfig;
import com.agroflow2.inventario_service.dto.CosechaDTO;
import com.agroflow2.inventario_service.model.Insumo;
import com.agroflow2.inventario_service.service.InventarioService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventario")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping("/stock/{nombreInsumo}")
    public Insumo obtenerStock(@PathVariable String nombreInsumo) {
        return inventarioService.obtenerInsumoPorNombre(nombreInsumo);
    }

    @RabbitListener(queues = "cola_nueva_cosecha")
    public void consumirCosecha(CosechaDTO cosechaDTO) {
        System.out.println("Recibiendo cosecha: " + cosechaDTO.getProducto());
        inventarioService.procesarCosecha(cosechaDTO);
    }
}