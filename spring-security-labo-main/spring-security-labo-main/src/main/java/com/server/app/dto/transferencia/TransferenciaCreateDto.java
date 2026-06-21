package com.server.app.dto.transferencia;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransferenciaCreateDto {

    @NotNull(message = "La cuenta de origen es obligatoria")
    private Long cuentaOrigenId;

    @NotNull(message = "La cuenta de destino es obligatoria")
    private Long cuentaDestinoId;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(
            value = "0.01",
            message = "El monto debe ser mayor que cero"
    )
    private BigDecimal monto;

    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;

    private String descripcion;
}
