package com.server.app.dto.prestamo;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PrestamoCreateDto {

    @NotNull(message = "El capital solicitado es obligatorio")
    @DecimalMin(
            value = "0.01",
            message = "El capital solicitado debe ser mayor que cero"
    )
    private BigDecimal capitalSolicitado;

    @NotNull(message = "La tasa de interés anual es obligatoria")
    @DecimalMin(
            value = "0.0",
            message = "La tasa de interés anual no puede ser negativa"
    )
    private BigDecimal tasaInteresAnual;

    @NotNull(message = "El plazo en meses es obligatorio")
    @Min(value = 1, message = "El plazo debe ser de al menos 1 mes")
    @Max(value = 360, message = "El plazo no puede superar 360 meses")
    private Integer plazoMeses;
}
