package com.server.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.app.entities.enums.TipoCategoria;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoCategoria tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_padre_id")
    @JsonIgnore
    private Categoria categoriaPadre;

    @OneToMany(mappedBy = "categoriaPadre")
    @JsonIgnore
    private List<Categoria> subcategorias = new ArrayList<>();

    @OneToMany(mappedBy = "categoria")
    @JsonIgnore
    private List<Movimiento> movimientos = new ArrayList<>();
}
