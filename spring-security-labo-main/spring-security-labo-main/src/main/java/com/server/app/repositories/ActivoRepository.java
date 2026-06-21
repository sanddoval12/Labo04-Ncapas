package com.server.app.repositories;

import com.server.app.entities.Activo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivoRepository extends JpaRepository<Activo, Long> {

    Optional<Activo> findBySimboloIgnoreCase(String simbolo);

    Page<Activo> findAllByOrderBySimboloAsc(Pageable pageable);
}