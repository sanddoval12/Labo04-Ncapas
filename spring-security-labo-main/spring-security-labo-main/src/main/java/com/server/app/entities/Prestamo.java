package com.server.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.app.entities.enums.EstadoPrestamo;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prestamos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prestamo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "capital_solicitado",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal capitalSolicitado;

    @Column(
            name = "tasa_interes_anual",
            nullable = false,
            precision = 9,
            scale = 4
    )
    private BigDecimal tasaInteresAnual;

    @Column(
            name = "plazo_meses",
            nullable = false
    )
    private Integer plazoMeses;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoPrestamo estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private User usuario;

    @OneToMany(
            mappedBy = "prestamo",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<PlanPago> planesPago = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (estado == null) {
            estado = EstadoPrestamo.PENDIENTE;
        }
    }
}
