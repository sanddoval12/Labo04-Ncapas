package com.server.app.dto.activo;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ActivoCreateDto {

    @NotBlank(message = "El símbolo es obligatorio")
    @Size(max = 20, message = "El símbolo no puede superar los 20 caracteres")
    private String simbolo;

    @NotBlank(message = "El mercado es obligatorio")
    @Size(max = 100, message = "El mercado no puede superar los 100 caracteres")
    private String mercado;

    @NotNull(message = "El precio de mercado es obligatorio")
    @DecimalMin(
            value = "0.01",
            message = "El precio de mercado debe ser mayor que cero"
    )
    private BigDecimal precioMercado;

    @NotBlank(message = "El sector es obligatorio")
    @Size(max = 100, message = "El sector no puede superar los 100 caracteres")
    private String sector;
}