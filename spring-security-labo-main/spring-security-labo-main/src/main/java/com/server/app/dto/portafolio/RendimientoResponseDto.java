package com.server.app.dto.portafolio;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class RendimientoResponseDto {

    private Long portafolioId;
    private String nombre;
    private BigDecimal capitalInvertido;
    private BigDecimal valorActual;
    private BigDecimal gananciaPerdida;
    private BigDecimal porcentajeRendimiento;
}