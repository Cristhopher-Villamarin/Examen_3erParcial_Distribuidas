package com.agroflow.cosecha_service.service;

import com.agroflow.cosecha_service.dto.CosechaDTO;
import com.agroflow.cosecha_service.model.Agricultor;
import com.agroflow.cosecha_service.model.Cosecha;
import com.agroflow.cosecha_service.repository.CosechaRepository;
import com.agroflow.cosecha_service.repository.AgricultorRepository;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CosechaService {

    private final CosechaRepository cosechaRepository;
    private final AgricultorRepository agricultorRepository;
    private final RabbitTemplate rabbitTemplate;

    public CosechaService(CosechaRepository cosechaRepository,
                          AgricultorRepository agricultorRepository,
                          RabbitTemplate rabbitTemplate) {
        this.cosechaRepository = cosechaRepository;
        this.agricultorRepository = agricultorRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Cosecha registrarCosecha(CosechaDTO cosechaDTO) {
        Long agricultorId = Long.parseLong(cosechaDTO.getAgricultorId());

        Agricultor agricultor = agricultorRepository.findById(agricultorId)
                .orElseThrow(() -> new RuntimeException("Agricultor no encontrado"));

        Cosecha cosecha = new Cosecha();
        cosecha.setProducto(cosechaDTO.getProducto());
        cosecha.setToneladas(cosechaDTO.getTonelada());
        cosecha.setUbicacion(cosechaDTO.getUbicacion());
        cosecha.setEstado("REGISTRADA");
        cosecha.setFechaRegistro(LocalDateTime.now());
        cosecha.setAgricultor(agricultor);

        cosechaRepository.save(cosecha);

        rabbitTemplate.convertAndSend("cosechaExchange", "cosechaQueue", cosecha);

        return cosecha;
    }

    public void actualizarEstado(Long cosechaId, String estado, String facturaId) {
        Cosecha cosecha = cosechaRepository.findById(cosechaId)
                .orElseThrow(() -> new RuntimeException("Cosecha no encontrada"));

        cosecha.setEstado(estado);
        cosechaRepository.save(cosecha);
    }
}
