package com.server.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.server.app.entities.enums.EstadoCuota;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "planes_pago")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlanPago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "numero_cuota",
            nullable = false
    )
    private Integer numeroCuota;

    @Column(
            name = "monto_capital",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal montoCapital;

    @Column(
            name = "monto_interes",
            nullable = false,
            precision = 19,
            scale = 2
    )
    private BigDecimal montoInteres;

    @Column(
            name = "fecha_vencimiento",
            nullable = false
    )
    private LocalDate fechaVencimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoCuota estado;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "prestamo_id", nullable = false)
    @JsonIgnore
    private Prestamo prestamo;

    @OneToMany(
            mappedBy = "planPago",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonIgnore
    private List<Abono> abonos = new ArrayList<>();

    @PrePersist
    public void prePersist() {
        if (estado == null) {
            estado = EstadoCuota.PENDIENTE;
        }
    }
}
