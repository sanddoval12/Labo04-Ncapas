package com.server.app.dto.movimiento;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class MovimientoResponseDto {

    private Long id;
    private BigDecimal monto;
    private String monedaOriginal;
    private BigDecimal tasaCambio;
    private LocalDateTime fecha;
    private String descripcion;

    private Long cuentaId;
    private String cuentaAlias;

    private Long categoriaId;
    private String categoriaNombre;

    private String transferenciaId;
}
