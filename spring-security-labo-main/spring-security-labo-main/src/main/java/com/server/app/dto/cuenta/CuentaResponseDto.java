package com.server.app.dto.cuenta;

import com.server.app.entities.enums.TipoCuenta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class CuentaResponseDto {

    private Long id;
    private String alias;
    private String moneda;
    private BigDecimal saldoBase;
    private TipoCuenta tipo;
}
