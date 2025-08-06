package com.agroflow3.facturacion_service.service;

import com.agroflow3.facturacion_service.dto.CosechaDTO;
import com.agroflow3.facturacion_service.dto.EstadoCosechaDto;
import com.agroflow3.facturacion_service.model.Factura;
import com.agroflow3.facturacion_service.repository.FacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FacturaService {

    private static final Map<String, Double> PRECIOS = new HashMap<>();

    static {
        PRECIOS.put("Arroz Oro", 120.0);
        PRECIOS.put("Café Premium", 300.0);
    }

    @Autowired
    private FacturaRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Factura> listarTodas() {
        return repository.findAll();
    }

    public void procesarCosecha(CosechaDTO dto) {
        // Calcular el monto
        Double precio = PRECIOS.getOrDefault(dto.getProducto(), 100.0); // Precio por defecto: $100
        Double monto = dto.getCantidad() * precio;

        // Crear y guardar la factura
        Factura factura = new Factura();
        factura.setCosechaId(dto.getCosechaId());
        factura.setMonto(monto);
        factura.setPagada(false);
        Factura savedFactura = repository.save(factura);

        // Notificar al microservicio Central
        EstadoCosechaDto estadoDto = new EstadoCosechaDto("FACTURADA", savedFactura.getFacturaId());
        restTemplate.put("http://localhost:8080/cosechas/{cosechaId}/estado",
                estadoDto,
                dto.getCosechaId());
    }
}
