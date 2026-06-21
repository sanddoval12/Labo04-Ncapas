package com.server.app.services;

import com.server.app.dto.inversion.InversionCreateDto;
import com.server.app.dto.inversion.InversionResponseDto;
import com.server.app.entities.Activo;
import com.server.app.entities.Inversion;
import com.server.app.entities.Portafolio;
import com.server.app.entities.enums.EstadoInversion;
import com.server.app.repositories.InversionRepository;
import com.server.app.repositories.PortafolioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class InversionService {

    private final InversionRepository inversionRepository;
    private final PortafolioRepository portafolioRepository;
    private final PortafolioService portafolioService;
    private final ActivoService activoService;

    @Transactional
    public InversionResponseDto create(
            int userId,
            InversionCreateDto dto
    ) {
        Portafolio portafolio =
                portafolioService.findEntityByIdAndUser(
                        dto.getPortafolioId(),
                        userId
                );

        Activo activo =
                activoService.findEntityById(dto.getActivoId());

        BigDecimal precioCompra =
                activo.getPrecioMercado();

        BigDecimal montoInvertido =
                dto.getCantidad().multiply(precioCompra);

        Inversion inversion = new Inversion();
        inversion.setCantidad(dto.getCantidad());
        inversion.setPrecioCompra(precioCompra);
        inversion.setFecha(LocalDateTime.now());
        inversion.setEstado(EstadoInversion.ABIERTA);
        inversion.setPortafolio(portafolio);
        inversion.setActivo(activo);

        Inversion savedInvestment =
                inversionRepository.save(inversion);

        portafolio.setBalanceTotal(
                portafolio.getBalanceTotal()
                        .add(montoInvertido)
        );

        portafolioRepository.save(portafolio);

        return toResponseDto(savedInvestment);
    }

    private InversionResponseDto toResponseDto(
            Inversion inversion
    ) {
        return new InversionResponseDto(
                inversion.getId(),
                inversion.getCantidad(),
                inversion.getPrecioCompra(),
                inversion.getFecha(),
                inversion.getEstado(),
                inversion.getPortafolio().getId(),
                inversion.getPortafolio().getNombre(),
                inversion.getActivo().getId(),
                inversion.getActivo().getSimbolo()
        );
    }
}