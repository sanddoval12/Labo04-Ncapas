package com.server.app.services;

import com.server.app.dto.activo.ActivoCreateDto;
import com.server.app.dto.activo.ActivoResponseDto;
import com.server.app.entities.Activo;
import com.server.app.exceptions.ConfictException;
import com.server.app.exceptions.NotFoundException;
import com.server.app.repositories.ActivoRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ActivoService {

    private final ActivoRepository activoRepository;

    @Transactional(readOnly = true)
    public Page<ActivoResponseDto> findAll(
            int page,
            int size
    ) {
        return activoRepository
                .findAllByOrderBySimboloAsc(
                        PageRequest.of(page, size)
                )
                .map(this::toResponseDto);
    }

    @Transactional
    public ActivoResponseDto create(ActivoCreateDto dto) {
        String simbolo = dto.getSimbolo()
                .trim()
                .toUpperCase();

        activoRepository
                .findBySimboloIgnoreCase(simbolo)
                .ifPresent(activo -> {
                    throw new ConfictException(
                            "Ya existe un activo con ese símbolo"
                    );
                });

        Activo activo = new Activo();
        activo.setSimbolo(simbolo);
        activo.setMercado(dto.getMercado());
        activo.setPrecioMercado(dto.getPrecioMercado());
        activo.setSector(dto.getSector());

        return toResponseDto(
                activoRepository.save(activo)
        );
    }

    @Transactional(readOnly = true)
    public Activo findEntityById(Long id) {
        return activoRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Activo no encontrado"
                        )
                );
    }

    private ActivoResponseDto toResponseDto(Activo activo) {
        return new ActivoResponseDto(
                activo.getId(),
                activo.getSimbolo(),
                activo.getMercado(),
                activo.getPrecioMercado(),
                activo.getSector()
        );
    }
}