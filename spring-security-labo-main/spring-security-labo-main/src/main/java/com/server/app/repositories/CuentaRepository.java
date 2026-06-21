package com.server.app.repositories;

import com.server.app.entities.Cuenta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {

    Page<Cuenta> findAllByUsuarioId(Integer usuarioId, Pageable pageable);

    Optional<Cuenta> findByIdAndUsuarioId(Long id, Integer usuarioId);
}
