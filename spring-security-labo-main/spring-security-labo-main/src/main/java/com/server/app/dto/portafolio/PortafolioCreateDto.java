package com.server.app.dto.portafolio;

import com.server.app.entities.enums.RiesgoPerfil;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PortafolioCreateDto {

    @NotBlank(message = "El nombre del portafolio es obligatorio")
    @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
    private String nombre;

    @NotNull(message = "El perfil de riesgo es obligatorio")
    private RiesgoPerfil riesgoPerfil;
}