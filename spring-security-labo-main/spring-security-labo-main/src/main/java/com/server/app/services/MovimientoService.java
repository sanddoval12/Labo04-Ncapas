package com.server.app.services;

import com.server.app.dto.movimiento.MovimientoResponseDto;
import com.server.app.entities.Movimiento;
import com.server.app.repositories.MovimientoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;

    @Transactional(readOnly = true)
    public Page<MovimientoResponseDto> findAllByUser(
            int userId,
            LocalDateTime desde,
            LocalDateTime hasta,
            int page,
            int size
    ) {
        PageRequest pageRequest = PageRequest.of(page, size);

        if (desde != null && hasta != null) {
            return movimientoRepository
                    .findAllByCuentaUsuarioIdAndFechaBetween(
                            userId,
                            desde,
                            hasta,
                            pageRequest
                    )
                    .map(this::toResponseDto);
        }

        if (desde != null) {
            return movimientoRepository
                    .findAllByCuentaUsuarioIdAndFechaGreaterThanEqual(
                            userId,
                            desde,
                            pageRequest
                    )
                    .map(this::toResponseDto);
        }

        if (hasta != null) {
            return movimientoRepository
                    .findAllByCuentaUsuarioIdAndFechaLessThanEqual(
                            userId,
                            hasta,
                            pageRequest
                    )
                    .map(this::toResponseDto);
        }

        return movimientoRepository
                .findAllByCuentaUsuarioId(userId, pageRequest)
                .map(this::toResponseDto);
    }

    public MovimientoResponseDto toResponseDto(Movimiento movimiento) {
        return new MovimientoResponseDto(
                movimiento.getId(),
                movimiento.getMonto(),
                movimiento.getMonedaOriginal(),
                movimiento.getTasaCambio(),
                movimiento.getFecha(),
                movimiento.getDescripcion(),
                movimiento.getCuenta().getId(),
                movimiento.getCuenta().getAlias(),
                movimiento.getCategoria().getId(),
                movimiento.getCategoria().getNombre(),
                movimiento.getTransferenciaId()
        );
    }
}
