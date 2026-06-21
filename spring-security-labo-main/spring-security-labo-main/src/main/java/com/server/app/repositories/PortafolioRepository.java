package com.server.app.repositories;

import com.server.app.entities.Portafolio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PortafolioRepository extends JpaRepository<Portafolio, Long> {

    Page<Portafolio> findAllByUsuarioId(Integer usuarioId, Pageable pageable);

    Optional<Portafolio> findByIdAndUsuarioId(Long id, Integer usuarioId);
}