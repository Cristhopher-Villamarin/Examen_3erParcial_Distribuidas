package com.agroflow2.inventario_service.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CosechaDTO {
    private String cosechaId;
    private String producto;
    private double cantidad;
}