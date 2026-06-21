package com.server.app.repositories;

import com.server.app.entities.Prestamo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    Page<Prestamo> findAllByUsuarioId(Integer usuarioId, Pageable pageable);

    List<Prestamo> findAllByUsuarioId(Integer usuarioId);

    Optional<Prestamo> findByIdAndUsuarioId(Long id, Integer usuarioId);
}
