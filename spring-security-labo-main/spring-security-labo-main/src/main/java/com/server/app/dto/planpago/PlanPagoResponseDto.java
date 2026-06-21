package com.server.app.dto.planpago;

import com.server.app.entities.enums.EstadoCuota;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class PlanPagoResponseDto {

    private Long id;
    private Integer numeroCuota;
    private BigDecimal montoCapital;
    private BigDecimal montoInteres;
    private LocalDate fechaVencimiento;
    private EstadoCuota estado;
    private Long prestamoId;
}
