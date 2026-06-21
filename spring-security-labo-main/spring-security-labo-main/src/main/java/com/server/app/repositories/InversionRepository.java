package com.server.app.repositories;

import com.server.app.entities.Inversion;
import com.server.app.entities.enums.EstadoInversion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InversionRepository extends JpaRepository<Inversion, Long> {

    List<Inversion> findAllByPortafolioIdAndEstado(
            Long portafolioId,
            EstadoInversion estado
    );
}