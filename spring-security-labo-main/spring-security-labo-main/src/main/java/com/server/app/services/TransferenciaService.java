package com.server.app.services;

import com.server.app.dto.transferencia.TransferenciaCreateDto;
import com.server.app.dto.transferencia.TransferenciaResponseDto;
import com.server.app.entities.Categoria;
import com.server.app.entities.Cuenta;
import com.server.app.entities.Movimiento;
import com.server.app.exceptions.BadRequestException;
import com.server.app.repositories.CuentaRepository;
import com.server.app.repositories.MovimientoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TransferenciaService {

    private final CuentaService cuentaService;
    private final CategoriaService categoriaService;
    private final CuentaRepository cuentaRepository;
    private final MovimientoRepository movimientoRepository;
    private final MovimientoService movimientoService;

    @Transactional
    public TransferenciaResponseDto realizarTransferencia(
            int userId,
            TransferenciaCreateDto dto
    ) {
        if (dto.getCuentaOrigenId().equals(dto.getCuentaDestinoId())) {
            throw new BadRequestException(
                    "La cuenta de origen y destino no pueden ser la misma"
            );
        }


        Cuenta cuentaOrigen = cuentaService.findEntityByIdAndUser(
                dto.getCuentaOrigenId(),
                userId
        );

        Cuenta cuentaDestino = cuentaService.findEntityById(
                dto.getCuentaDestinoId()
        );

        Categoria categoria = categoriaService.findEntityById(
                dto.getCategoriaId()
        );

        BigDecimal monto = dto.getMonto();

        if (cuentaOrigen.getSaldoBase().compareTo(monto) < 0) {
            throw new BadRequestException(
                    "Fondos insuficientes en la cuenta de origen"
            );
        }

        String transferenciaId = UUID.randomUUID().toString();
        LocalDateTime fecha = LocalDateTime.now();

        cuentaOrigen.setSaldoBase(
                cuentaOrigen.getSaldoBase().subtract(monto)
        );
        cuentaDestino.setSaldoBase(
                cuentaDestino.getSaldoBase().add(monto)
        );

        cuentaRepository.save(cuentaOrigen);
        cuentaRepository.save(cuentaDestino);

        Movimiento movimientoEgreso = crearMovimiento(
                monto.negate(),
                cuentaOrigen,
                categoria,
                transferenciaId,
                fecha,
                dto.getDescripcion()
        );

        Movimiento movimientoIngreso = crearMovimiento(
                monto,
                cuentaDestino,
                categoria,
                transferenciaId,
                fecha,
                dto.getDescripcion()
        );

        movimientoRepository.save(movimientoEgreso);
        movimientoRepository.save(movimientoIngreso);

        return new TransferenciaResponseDto(
                transferenciaId,
                movimientoService.toResponseDto(movimientoEgreso),
                movimientoService.toResponseDto(movimientoIngreso)
        );
    }

    private Movimiento crearMovimiento(
            BigDecimal monto,
            Cuenta cuenta,
            Categoria categoria,
            String transferenciaId,
            LocalDateTime fecha,
            String descripcion
    ) {
        Movimiento movimiento = new Movimiento();
        movimiento.setMonto(monto);
        movimiento.setMonedaOriginal(cuenta.getMoneda());
        movimiento.setTasaCambio(BigDecimal.ONE);
        movimiento.setFecha(fecha);
        movimiento.setDescripcion(descripcion);
        movimiento.setCuenta(cuenta);
        movimiento.setCategoria(categoria);
        movimiento.setTransferenciaId(transferenciaId);

        return movimiento;
    }
}
