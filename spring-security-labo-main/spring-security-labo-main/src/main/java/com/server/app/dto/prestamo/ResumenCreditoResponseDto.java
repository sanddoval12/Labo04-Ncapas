package com.server.app.dto.prestamo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ResumenCreditoResponseDto {

    private int cantidadPrestamosActivos;
    private BigDecimal capitalTotalSolicitado;
    private BigDecimal saldoCapitalPendiente;
    private BigDecimal interesPendiente;
    private BigDecimal moraAcumulada;
    private BigDecimal deudaTotalPendiente;
    private int cuotasPendientes;
    private int cuotasVencidas;
}
