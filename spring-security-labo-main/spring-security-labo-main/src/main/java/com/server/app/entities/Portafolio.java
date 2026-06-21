package com.server.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.app.entities.enums.RiesgoPerfil;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "portafolios")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Portafolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(
            name = "balance_total",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal balanceTotal = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "riesgo_perfil",
            nullable = false,
            length = 20
    )
    private RiesgoPerfil riesgoPerfil;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private User usuario;

    @OneToMany(
            mappedBy = "portafolio",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<Inversion> inversiones = new ArrayList<>();
}