package com.server.app.repositories;

import com.server.app.entities.Abono;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbonoRepository extends JpaRepository<Abono, Long> {

    List<Abono> findAllByPlanPagoIdOrderByFechaPagoAsc(Long planPagoId);

    List<Abono> findAllByPlanPagoPrestamoId(Long prestamoId);
}
