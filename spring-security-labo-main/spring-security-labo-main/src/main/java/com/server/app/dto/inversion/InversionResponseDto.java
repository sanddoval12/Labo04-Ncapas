package com.server.app.dto.inversion;

import com.server.app.entities.enums.EstadoInversion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class InversionResponseDto {

    private Long id;
    private BigDecimal cantidad;
    private BigDecimal precioCompra;
    private LocalDateTime fecha;
    private EstadoInversion estado;

    private Long portafolioId;
    private String portafolioNombre;

    private Long activoId;
    private String activoSimbolo;
}