package com.server.app.dto.categoria;

import com.server.app.entities.enums.TipoCategoria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoriaResponseDto {

    private Long id;
    private String nombre;
    private TipoCategoria tipo;
    private Long categoriaPadreId;
}
