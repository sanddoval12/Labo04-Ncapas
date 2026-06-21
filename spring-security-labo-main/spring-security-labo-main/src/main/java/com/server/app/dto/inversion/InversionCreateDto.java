package com.server.app.dto.inversion;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class InversionCreateDto {

    @NotNull(message = "El portafolio es obligatorio")
    private Long portafolioId;

    @NotNull(message = "El activo es obligatorio")
    private Long activoId;

    @NotNull(message = "La cantidad es obligatoria")
    @DecimalMin(
            value = "0.000001",
            message = "La cantidad debe ser mayor que cero"
    )
    private BigDecimal cantidad;
}