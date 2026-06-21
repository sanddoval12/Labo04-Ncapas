package com.server.app.dto.abono;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class AbonoCreateDto {

    @NotNull(message = "El plan de pago es obligatorio")
    private Long planPagoId;

    @NotNull(message = "El monto del abono es obligatorio")
    @DecimalMin(
            value = "0.01",
            message = "El monto del abono debe ser mayor que cero"
    )
    private BigDecimal monto;

    private LocalDate fechaPago;
}
