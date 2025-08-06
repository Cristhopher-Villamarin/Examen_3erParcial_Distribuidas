package com.agroflow.cosecha_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class CosechaDTO {
    private String agricultorId;
    private String producto;
    private Double tonelada;
    private String ubicacion;
}
