package com.server.app.dto.abono;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class AbonoResponseDto {

    private Long id;
    private BigDecimal monto;
    private LocalDate fechaPago;
    private BigDecimal recargoMora;
    private Long planPagoId;
}
