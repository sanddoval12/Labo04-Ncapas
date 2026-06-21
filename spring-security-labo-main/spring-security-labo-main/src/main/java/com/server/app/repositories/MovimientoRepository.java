package com.server.app.repositories;

import com.server.app.entities.Movimiento;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    Page<Movimiento> findAllByCuentaUsuarioIdAndFechaBetween(
            Integer usuarioId,
            LocalDateTime desde,
            LocalDateTime hasta,
            Pageable pageable
    );

    Page<Movimiento> findAllByCuentaUsuarioIdAndFechaGreaterThanEqual(
            Integer usuarioId,
            LocalDateTime desde,
            Pageable pageable
    );

    Page<Movimiento> findAllByCuentaUsuarioIdAndFechaLessThanEqual(
            Integer usuarioId,
            LocalDateTime hasta,
            Pageable pageable
    );

    Page<Movimiento> findAllByCuentaUsuarioId(
            Integer usuarioId,
            Pageable pageable
    );
}
