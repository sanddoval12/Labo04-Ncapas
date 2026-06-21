package com.server.app.dto.cuenta;

import com.server.app.entities.enums.TipoCuenta;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CuentaCreateDto {

    @NotBlank(message = "El alias de la cuenta es obligatorio")
    @Size(max = 100, message = "El alias no puede superar los 100 caracteres")
    private String alias;

    @NotBlank(message = "La moneda es obligatoria")
    @Size(max = 10, message = "La moneda no puede superar los 10 caracteres")
    private String moneda;

    @NotNull(message = "El tipo de cuenta es obligatorio")
    private TipoCuenta tipo;
}
