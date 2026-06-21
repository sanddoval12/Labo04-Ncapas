package com.server.app.dto.activo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ActivoResponseDto {

    private Long id;
    private String simbolo;
    private String mercado;
    private BigDecimal precioMercado;
    private String sector;
}