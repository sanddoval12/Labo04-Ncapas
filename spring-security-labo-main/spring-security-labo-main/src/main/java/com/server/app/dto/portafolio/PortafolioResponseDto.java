package com.server.app.dto.portafolio;

import com.server.app.entities.enums.RiesgoPerfil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class PortafolioResponseDto {

    private Long id;
    private String nombre;
    private BigDecimal balanceTotal;
    private RiesgoPerfil riesgoPerfil;
}