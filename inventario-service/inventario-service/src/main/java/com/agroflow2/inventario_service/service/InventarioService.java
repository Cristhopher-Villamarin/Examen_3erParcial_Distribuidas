package com.agroflow2.inventario_service.service;


import com.agroflow2.inventario_service.config.RabbitMQConfig;
import com.agroflow2.inventario_service.dto.CosechaDTO;
import com.agroflow2.inventario_service.dto.InsumoAjustadoDTO;
import com.agroflow2.inventario_service.model.Insumo;
import com.agroflow2.inventario_service.repository.InsumoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InventarioService {

    private static final double STOCK_INICIAL = 200.0;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void procesarCosecha(CosechaDTO cosechaDTO) {
        String nombreInsumo = cosechaDTO.getProducto();
        double cantidadCosecha = cosechaDTO.getTonelada();

        // Buscar si el insumo ya existe
        Insumo insumo = insumoRepository.findByNombreInsumo(nombreInsumo);
        if (insumo == null) {
            // Nuevo insumo: inicializar con stock de 200
            insumo = new Insumo(null, nombreInsumo, STOCK_INICIAL);
        }

        // Validar y actualizar stock
        double nuevoStock = insumo.getStock() - cantidadCosecha;
        if (nuevoStock < 0) {
            throw new RuntimeException("Stock insuficiente para el insumo: " + nombreInsumo);
        }
        insumo.setStock(nuevoStock);
        insumoRepository.save(insumo);


        // Publicar evento en cola_inventario_ajustado
        InsumoAjustadoDTO ajusteDTO = new InsumoAjustadoDTO(
                cosechaDTO.getCosechaId(),
                nombreInsumo,
                cantidadCosecha,
                nuevoStock
        );
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY_INVENTARIO, ajusteDTO);
    }

    public Insumo obtenerInsumoPorNombre(String nombreInsumo) {
        Insumo insumo = insumoRepository.findByNombreInsumo(nombreInsumo);
        if (insumo == null) {
            throw new RuntimeException("Insumo no encontrado: " + nombreInsumo);
        }
        return insumo;
    }
}