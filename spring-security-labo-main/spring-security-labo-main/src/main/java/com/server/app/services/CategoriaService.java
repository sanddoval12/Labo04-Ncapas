package com.server.app.services;

import com.server.app.dto.categoria.CategoriaResponseDto;
import com.server.app.entities.Categoria;
import com.server.app.exceptions.NotFoundException;
import com.server.app.repositories.CategoriaRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    @Transactional(readOnly = true)
    public Page<CategoriaResponseDto> findAll(int page, int size) {
        return categoriaRepository
                .findAllByOrderByNombreAsc(PageRequest.of(page, size))
                .map(this::toResponseDto);
    }

    @Transactional(readOnly = true)
    public Categoria findEntityById(Long id) {
        return categoriaRepository
                .findById(id)
                .orElseThrow(() ->
                        new NotFoundException(
                                "Categoría no encontrada"
                        )
                );
    }

    private CategoriaResponseDto toResponseDto(Categoria categoria) {
        Long categoriaPadreId = categoria.getCategoriaPadre() != null
                ? categoria.getCategoriaPadre().getId()
                : null;

        return new CategoriaResponseDto(
                categoria.getId(),
                categoria.getNombre(),
                categoria.getTipo(),
                categoriaPadreId
        );
    }
}
