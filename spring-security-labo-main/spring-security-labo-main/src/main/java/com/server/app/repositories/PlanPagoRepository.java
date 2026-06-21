package com.server.app.repositories;

import com.server.app.entities.PlanPago;
import com.server.app.entities.enums.EstadoCuota;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlanPagoRepository extends JpaRepository<PlanPago, Long> {

    List<PlanPago> findAllByPrestamoIdOrderByNumeroCuotaAsc(Long prestamoId);

    List<PlanPago> findAllByPrestamoIdAndEstadoOrderByNumeroCuotaAsc(
            Long prestamoId,
            EstadoCuota estado
    );

    Page<PlanPago> findAllByPrestamoIdAndEstado(
            Long prestamoId,
            EstadoCuota estado,
            Pageable pageable
    );

    Optional<PlanPago> findByIdAndPrestamoId(Long id, Long prestamoId);
}
