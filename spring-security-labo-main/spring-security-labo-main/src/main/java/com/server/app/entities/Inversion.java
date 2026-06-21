package com.server.app.entities;

import com.server.app.entities.enums.EstadoInversion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "inversiones")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Inversion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            nullable = false,
            precision = 19,
            scale = 6
    )
    private BigDecimal cantidad;

    @Column(
            name = "precio_compra",
            nullable = false,
            precision = 19,
            scale = 4
    )
    private BigDecimal precioCompra;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoInversion estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "portafolio_id", nullable = false)
    private Portafolio portafolio;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "activo_id", nullable = false)
    private Activo activo;

    @PrePersist
    public void prePersist() {
        if (fecha == null) {
            fecha = LocalDateTime.now();
        }

        if (estado == null) {
            estado = EstadoInversion.ABIERTA;
        }
    }
}